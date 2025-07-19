#!/usr/bin/env python3
"""
Módulo de autenticación para la API de MintScan
Utiliza solo la biblioteca estándar de Python

Uso desde terminal:
    python3 login.py <usuario>              # Solicita contraseña interactivamente
    python3 login.py <usuario> <contraseña>  # Menos seguro - visible en historial
    python3 login.py                         # Usa MINTSCAN_USER y MINTSCAN_PASS
    python3 login.py --help

SEGURIDAD: Se recomienda usar variables de entorno o entrada interactiva
para evitar exponer contraseñas en el historial de comandos.
"""

import json
import urllib.request
import urllib.error
import sys
import argparse
import os
import getpass
from typing import Dict, Tuple, TypedDict, Literal, Union


# Tipos de respuesta de la API
class LoginResponse(TypedDict):
    """Respuesta exitosa del endpoint de login"""
    token: str


class ErrorResponse(TypedDict):
    """Respuesta de error de la API"""
    title: str
    detail: str
    code: Union[
        Literal['NOT_AUTHORIZED'],
        Literal['TOKEN_NOT_PROVIDED'],
        Literal['INVALID_TOKEN'],
        Literal['INVALID_SIGNATURE'],
        Literal['EXPIRED_TOKEN'],
        Literal['INVALID_CREDENTIALS'],
        Literal['INVALID_FORMAT'],
        Literal['INTERNAL_SERVER_ERROR']
    ]
    status: int


def iniciar_sesion(usuario: str, contraseña: str) -> str:
    """
    Iniciar sesión en la API de MintScan y obtener token de autenticación
    
    Args:
        usuario: Nombre de usuario
        contraseña: Contraseña del usuario
        
    Returns:
        str: Token de autenticación JWT
        
    Raises:
        Exception: Si falla el inicio de sesión o hay error de red
    """
    url = "https://rest.mintitv.com/api/v1/login"
    
    # Preparar datos de la petición
    datos = {
        "username": usuario,
        "password": contraseña
    }
    
    # Convertir a bytes JSON
    datos_json = json.dumps(datos).encode('utf-8')
    
    # Crear petición
    peticion = urllib.request.Request(
        url,
        data=datos_json,
        headers={
            'Content-Type': 'application/json',
            'Content-Length': str(len(datos_json))
        },
        method='POST'
    )
    
    try:
        # Realizar petición
        with urllib.request.urlopen(peticion) as respuesta:
            # Leer y decodificar respuesta
            datos_respuesta = respuesta.read().decode('utf-8')
            json_respuesta = json.loads(datos_respuesta)
            
            # Extraer token
            respuesta_tipada: LoginResponse = json_respuesta
            if 'token' in respuesta_tipada:
                return respuesta_tipada['token']
            else:
                raise Exception("No hay token en la respuesta")
                
    except urllib.error.HTTPError as e:
        # Manejar errores HTTP
        cuerpo_error = e.read().decode('utf-8')
        try:
            error_tipado: ErrorResponse = json.loads(cuerpo_error)
            codigo_error = error_tipado.get('code', '')
            
            # Mensajes de error más genéricos para evitar filtrar información
            if e.code == 401 or codigo_error == 'INVALID_CREDENTIALS':
                raise Exception("Credenciales inválidas")
            elif e.code == 403:
                raise Exception("Acceso denegado")
            elif e.code == 404:
                raise Exception("Servicio no disponible")
            elif e.code >= 500:
                raise Exception("Error del servidor")
            else:
                raise Exception(f"Error de autenticación (código: {e.code})")
        except json.JSONDecodeError:
            if e.code >= 500:
                raise Exception("Error del servidor")
            else:
                raise Exception(f"Error de comunicación (código: {e.code})")
    except urllib.error.URLError:
        raise Exception("No se pudo conectar al servidor")
    except Exception as e:
        # Evitar mostrar detalles técnicos específicos
        if "timed out" in str(e).lower():
            raise Exception("Tiempo de espera agotado")
        elif "connection" in str(e).lower():
            raise Exception("Error de conexión")
        else:
            raise Exception("Error durante el inicio de sesión")


def crear_cabecera_bearer(token: str) -> Dict[str, str]:
    """
    Crear cabecera de autorización con token Bearer
    
    Args:
        token: Token JWT
        
    Returns:
        dict: Cabeceras con autorización
    """
    return {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }


def main():
    """Función principal para ejecución desde línea de comandos"""
    parser = argparse.ArgumentParser(
        description='Iniciar sesión en la API de MintScan y obtener token JWT',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Ejemplos:
    python3 login.py miusuario
    python3 login.py usuario@ejemplo.com
    python3 login.py  # Usa variables de entorno MINTSCAN_USER y MINTSCAN_PASS
    python3 login.py --help

Variables de entorno:
    MINTSCAN_USER: Usuario para autenticación
    MINTSCAN_PASS: Contraseña del usuario
''')
    
    parser.add_argument('usuario', nargs='?', help='Nombre de usuario (opcional si se usa MINTSCAN_USER)')
    parser.add_argument('contraseña', nargs='?', help='Contraseña (opcional si se usa MINTSCAN_PASS o se solicita interactivamente)')
    parser.add_argument('-q', '--quiet', action='store_true', 
                       help='Solo mostrar el token, sin mensajes adicionales')
    parser.add_argument('-v', '--verbose', action='store_true',
                       help='Mostrar información detallada')
    parser.add_argument('--no-interactive', action='store_true',
                       help='No solicitar contraseña interactivamente')
    
    args = parser.parse_args()
    
    try:
        # Obtener usuario
        usuario = args.usuario
        if not usuario:
            usuario = os.environ.get('MINTSCAN_USER')
            if not usuario:
                print("Error: Se requiere usuario (argumento o variable MINTSCAN_USER)", file=sys.stderr)
                return 1
        
        # Obtener contraseña
        contraseña = args.contraseña
        if not contraseña:
            contraseña = os.environ.get('MINTSCAN_PASS')
            if not contraseña and not args.no_interactive:
                if not args.quiet:
                    print(f"Usuario: {usuario}")
                contraseña = getpass.getpass("Contraseña: ")
            elif not contraseña:
                print("Error: Se requiere contraseña (argumento, variable MINTSCAN_PASS o entrada interactiva)", file=sys.stderr)
                return 1
        
        # Iniciar sesión
        if args.verbose:
            print(f"Iniciando sesión...")
            print(f"Usuario: {usuario}")
        
        token = iniciar_sesion(usuario, contraseña)
        
        if args.quiet:
            # Solo imprimir el token
            print(token)
        else:
            print(f"¡Inicio de sesión exitoso!")
            print(f"Token: {token}")
            if args.verbose:
                print(f"\nPuedes usar este token en los otros scripts:")
                print(f"  python3 retrieve_process.py --token '{token}' <id_proceso>")
                print(f"  python3 list_processes.py --token '{token}'")
                print(f"  python3 process_image_pool.py --token '{token}' <archivo>")
        
        return 0
        
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return 1


# Ejecutar si es el script principal
if __name__ == "__main__":
    sys.exit(main())