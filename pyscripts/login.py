#!/usr/bin/env python3
"""
Módulo de autenticación para la API de MintITV
Utiliza solo la biblioteca estándar de Python

Uso desde terminal:
    python3 login.py <usuario> <contraseña>
    python3 login.py --help
"""

import json
import urllib.request
import urllib.error
import sys
import argparse
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
    Iniciar sesión en la API de MintITV y obtener token de autenticación
    
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
            mensaje_error = error_tipado.get('detail', 'Fallo en inicio de sesión')
            codigo_error = error_tipado.get('code', '')
            if codigo_error:
                mensaje_error = f"{mensaje_error} (Código: {codigo_error})"
        except json.JSONDecodeError:
            mensaje_error = f"HTTP {e.code}: {e.reason}"
        
        raise Exception(f"Fallo en inicio de sesión: {mensaje_error}")
    except Exception as e:
        raise Exception(f"Error de inicio de sesión: {str(e)}")


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
        description='Iniciar sesión en la API de MintITV y obtener token JWT',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Ejemplos:
    python3 login.py miusuario miclave
    python3 login.py usuario@ejemplo.com "mi_clave_segura"
    python3 login.py --help
''')
    
    parser.add_argument('usuario', help='Nombre de usuario para autenticación')
    parser.add_argument('contraseña', help='Contraseña del usuario')
    parser.add_argument('-q', '--quiet', action='store_true', 
                       help='Solo mostrar el token, sin mensajes adicionales')
    parser.add_argument('-v', '--verbose', action='store_true',
                       help='Mostrar información detallada')
    
    args = parser.parse_args()
    
    try:
        # Iniciar sesión
        if args.verbose:
            print(f"Iniciando sesión...")
            print(f"Usuario: {args.usuario}")
        
        token = iniciar_sesion(args.usuario, args.contraseña)
        
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