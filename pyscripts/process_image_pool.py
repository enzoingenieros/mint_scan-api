#!/usr/bin/env python3
"""
Módulo para procesar imágenes en pool de la API de MintITV
Utiliza solo la biblioteca estándar de Python

Uso desde terminal:
    python3 process_image_pool.py --token <token> --tipo <tipo> --categoria <cat> archivo1 [archivo2 ...]
    python3 process_image_pool.py --help
"""

import json
import base64
import urllib.request
import urllib.error
import uuid
import sys
import os
import argparse
from typing import Dict, List, Any, Union, Optional, TypedDict, Literal


# Tipos de la API
class ImageObject(TypedDict):
    """Objeto de imagen para procesar"""
    base64: str
    fileName: str
    fileType: Literal['image/jpeg', 'application/pdf', 'image/tiff', 'image/png']


class ProcessPoolRequest(TypedDict):
    """Petición para procesar imágenes"""
    id: str
    type: Literal['coc', 'titv-old', 'titv-new', 'reduced', 'single-approval', 'cdc']
    category: Literal['M1', 'M3', 'N1', 'N3', 'L', 'O', 'T', 'TR', 'OS', 'OSR']
    images: Union[ImageObject, List[ImageObject]]
    name: Optional[str]
    extractAccuracy: Optional[bool]


class ProcessPoolResponse(TypedDict):
    """Respuesta del endpoint de procesamiento"""
    success: bool
    id: str
    message: str


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


def procesar_imagenes_pool(
    token: str,
    id_proceso: str,
    tipo_doc: Literal['coc', 'titv-old', 'titv-new', 'reduced', 'single-approval', 'cdc'],
    categoria: Literal['M1', 'M3', 'N1', 'N3', 'L', 'O', 'T', 'TR', 'OS', 'OSR'],
    imagenes: Union[ImageObject, List[ImageObject]],
    nombre: Optional[str] = None,
    extraer_precision: bool = False
) -> ProcessPoolResponse:
    """
    Procesar imágenes en pool para reconocimiento de documentos
    
    Args:
        token: Token de autenticación JWT
        id_proceso: UUID para la operación (se generará si no se proporciona como UUID válido)
        tipo_doc: Tipo de documento (coc, titv-old, titv-new, reduced, single-approval, cdc)
        categoria: Categoría del vehículo (M1, M3, N1, N3, L, O, T, TR, OS, OSR)
        imagenes: Dict de imagen única o lista de dicts de imágenes, cada uno conteniendo:
            - base64: Datos de imagen codificados en Base64
            - fileName: Nombre del archivo incluyendo extensión
            - fileType: Tipo MIME (image/jpeg, application/pdf, image/tiff, image/png)
        nombre: Nombre opcional para fácil identificación (máx 100 caracteres)
        extraer_precision: Bandera opcional para cálculo de precisión (no recomendado para producción)
        
    Returns:
        ProcessPoolResponse: Respuesta con success, id y message
            
    Raises:
        Exception: Si falla la petición o error de validación
    """
    url = "https://rest.mintitv.com/api/v1/process/pool"
    
    # Validar UUID o generar uno nuevo
    try:
        uuid.UUID(id_proceso)
    except ValueError:
        id_proceso = str(uuid.uuid4())
    
    # Preparar datos de la petición
    datos: ProcessPoolRequest = {
        "id": id_proceso,
        "type": tipo_doc,
        "category": categoria,
        "images": imagenes if isinstance(imagenes, list) else [imagenes]
    }
    
    # Campos opcionales
    if nombre:
        datos["name"] = nombre[:100]  # Limitar a 100 caracteres
    
    if extraer_precision:
        datos["extractAccuracy"] = extraer_precision
    
    # Convertir a bytes JSON
    datos_json = json.dumps(datos).encode('utf-8')
    
    # Crear petición con autenticación
    cabeceras = crear_cabecera_bearer(token)
    cabeceras['Content-Length'] = str(len(datos_json))
    
    peticion = urllib.request.Request(
        url,
        data=datos_json,
        headers=cabeceras,
        method='POST'
    )
    
    try:
        # Realizar petición
        with urllib.request.urlopen(peticion) as respuesta:
            # Leer y decodificar respuesta
            datos_respuesta = respuesta.read().decode('utf-8')
            json_respuesta = json.loads(datos_respuesta)
            
            return json_respuesta
            
    except urllib.error.HTTPError as e:
        # Manejar errores HTTP
        cuerpo_error = e.read().decode('utf-8')
        try:
            json_error = json.loads(cuerpo_error)
            mensaje_error = json_error.get('detail', f'HTTP {e.code}')
            codigo_error = json_error.get('code', 'UNKNOWN_ERROR')
            
            if e.code == 422:
                if codigo_error == 'INVALID_FORMAT':
                    raise Exception(f"Formato inválido: {mensaje_error}")
                    
        except json.JSONDecodeError:
            mensaje_error = f"HTTP {e.code}: {e.reason}"
        
        raise Exception(f"Fallo al procesar imágenes: {mensaje_error}")
    except Exception as e:
        raise Exception(f"Error en la petición: {str(e)}")


def codificar_archivo_imagen(ruta_archivo: str) -> str:
    """
    Codificar archivo de imagen a base64
    
    Args:
        ruta_archivo: Ruta al archivo de imagen
        
    Returns:
        str: Datos de imagen codificados en Base64
    """
    with open(ruta_archivo, 'rb') as f:
        return base64.b64encode(f.read()).decode('utf-8')


def crear_objeto_imagen(ruta_archivo: str, nombre_archivo: Optional[str] = None) -> ImageObject:
    """
    Crear objeto de imagen para petición API
    
    Args:
        ruta_archivo: Ruta al archivo de imagen
        nombre_archivo: Nombre de archivo personalizado opcional (por defecto usa el nombre real)
        
    Returns:
        ImageObject: Objeto de imagen con base64, fileName y fileType
    """
    import os
    
    # Obtener nombre de archivo si no se proporciona
    if not nombre_archivo:
        nombre_archivo = os.path.basename(ruta_archivo)
    
    # Determinar tipo de archivo basado en la extensión
    ext = os.path.splitext(ruta_archivo)[1].lower()
    mapa_tipo_archivo = {
        '.jpg': 'image/jpeg',
        '.jpeg': 'image/jpeg',
        '.png': 'image/png',
        '.pdf': 'application/pdf',
        '.tiff': 'image/tiff',
        '.tif': 'image/tiff'
    }
    
    tipo_archivo = mapa_tipo_archivo.get(ext)
    if not tipo_archivo:
        raise ValueError(f"Tipo de archivo no soportado: {ext}")
    
    imagen: ImageObject = {
        'base64': codificar_archivo_imagen(ruta_archivo),
        'fileName': nombre_archivo,
        'fileType': tipo_archivo
    }
    return imagen


def procesar_multiples_archivos(
    token: str,
    rutas_archivos: List[str],
    tipo_doc: Literal['coc', 'titv-old', 'titv-new', 'reduced', 'single-approval', 'cdc'],
    categoria: Literal['M1', 'M3', 'N1', 'N3', 'L', 'O', 'T', 'TR', 'OS', 'OSR'],
    nombre: Optional[str] = None
) -> ProcessPoolResponse:
    """
    Procesar múltiples archivos de imagen
    
    Args:
        token: Token de autenticación JWT
        rutas_archivos: Lista de rutas a archivos de imagen
        tipo_doc: Tipo de documento
        categoria: Categoría del vehículo
        nombre: Nombre opcional para identificación
        
    Returns:
        ProcessPoolResponse: Respuesta de la API
    """
    # Crear objetos de imagen
    imagenes = []
    for ruta_archivo in rutas_archivos:
        try:
            objeto_imagen = crear_objeto_imagen(ruta_archivo)
            imagenes.append(objeto_imagen)
            print(f"Añadido: {ruta_archivo}")
        except Exception as e:
            print(f"Error procesando {ruta_archivo}: {e}")
    
    if not imagenes:
        raise Exception("No hay imágenes válidas para procesar")
    
    # Generar ID de proceso
    id_proceso = str(uuid.uuid4())
    
    # Procesar imágenes
    return procesar_imagenes_pool(
        token=token,
        id_proceso=id_proceso,
        tipo_doc=tipo_doc,
        categoria=categoria,
        imagenes=imagenes,
        nombre=nombre
    )


def main():
    """Función principal para ejecución desde línea de comandos"""
    parser = argparse.ArgumentParser(
        description='Procesar imágenes en la API de MintITV',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Ejemplos:
    # Procesar un solo archivo:
    python3 process_image_pool.py --token "$token" --tipo coc --categoria M1 documento.pdf
    
    # Procesar múltiples archivos:
    python3 process_image_pool.py --token "$token" --tipo titv-new --categoria N1 *.jpg
    
    # Con nombre descriptivo:
    python3 process_image_pool.py --token "$token" --tipo coc --categoria M1 --nombre "BMW 2024" doc.pdf
    
    # Especificar ID del proceso:
    python3 process_image_pool.py --token "$token" --tipo coc --categoria M1 --id 731cb083-7d83-4ce7-a0ce-1a3b19b7e422 imagen.jpg

Tipos de documento soportados:
    coc, titv-old, titv-new, reduced, single-approval, cdc

Categorías de vehículo:
    M1, M3, N1, N3, L, O, T, TR, OS, OSR
''')
    
    parser.add_argument('archivos', nargs='+', 
                       help='Archivos de imagen o PDF a procesar')
    parser.add_argument('--token', '-t', required=True,
                       help='Token JWT de autenticación (obtenido con login.py)')
    parser.add_argument('--tipo', '-tp', required=True,
                       choices=['coc', 'titv-old', 'titv-new', 'reduced', 
                               'single-approval', 'cdc'],
                       help='Tipo de documento')
    parser.add_argument('--categoria', '-c', required=True,
                       choices=['M1', 'M3', 'N1', 'N3', 'L', 'O', 'T', 'TR', 'OS', 'OSR'],
                       help='Categoría del vehículo')
    parser.add_argument('--nombre', '-n',
                       help='Nombre descriptivo para el proceso (máx 100 caracteres)')
    parser.add_argument('--id', '-i',
                       help='UUID del proceso (se genera automáticamente si no se especifica)')
    parser.add_argument('--precision', '-p', action='store_true',
                       help='Calcular precisión de extracción (experimental)')
    parser.add_argument('-v', '--verbose', action='store_true',
                       help='Mostrar información detallada')
    
    args = parser.parse_args()
    
    try:
        # Validar que los archivos existen
        archivos_validos = []
        for archivo in args.archivos:
            if not os.path.exists(archivo):
                print(f"Error: El archivo no existe: {archivo}", file=sys.stderr)
                continue
            if not os.path.isfile(archivo):
                print(f"Error: No es un archivo: {archivo}", file=sys.stderr)
                continue
            archivos_validos.append(archivo)
        
        if not archivos_validos:
            print("Error: No se encontraron archivos válidos para procesar", file=sys.stderr)
            return 1
        
        # Crear objetos de imagen
        imagenes = []
        for archivo in archivos_validos:
            try:
                imagen = crear_objeto_imagen(archivo)
                imagenes.append(imagen)
                if args.verbose:
                    print(f"Preparado: {archivo} ({imagen['fileType']})")
            except Exception as e:
                print(f"Error procesando {archivo}: {e}", file=sys.stderr)
        
        if not imagenes:
            print("Error: No se pudieron procesar los archivos", file=sys.stderr)
            return 1
        
        # Generar o validar ID del proceso
        if args.id:
            try:
                uuid.UUID(args.id)
                id_proceso = args.id
            except ValueError:
                print(f"Error: ID inválido (debe ser UUID): {args.id}", file=sys.stderr)
                return 1
        else:
            id_proceso = str(uuid.uuid4())
        
        if args.verbose:
            print(f"\nProcesando {len(imagenes)} archivo(s)...")
            print(f"ID del proceso: {id_proceso}")
            print(f"Tipo: {args.tipo}")
            print(f"Categoría: {args.categoria}")
            if args.nombre:
                print(f"Nombre: {args.nombre}")
        
        # Procesar imágenes
        resultado = procesar_imagenes_pool(
            token=args.token,
            id_proceso=id_proceso,
            tipo_doc=args.tipo,
            categoria=args.categoria,
            imagenes=imagenes if len(imagenes) > 1 else imagenes[0],
            nombre=args.nombre,
            extraer_precision=args.precision
        )
        
        # Mostrar resultado
        if resultado.get('success'):
            print(f"\n✓ Procesamiento iniciado exitosamente")
            print(f"ID del proceso: {resultado.get('id')}")
            print(f"\nPuedes verificar el estado con:")
            print(f"  python3 retrieve_process.py --token \"$token\" {resultado.get('id')}")
        else:
            print(f"\n✗ Error en el procesamiento")
            print(json.dumps(resultado, indent=2, ensure_ascii=False))
        
        return 0
        
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return 1


# Ejecutar si es el script principal
if __name__ == "__main__":
    sys.exit(main())