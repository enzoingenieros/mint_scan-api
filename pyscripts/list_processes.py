#!/usr/bin/env python3
"""
Módulo para listar documentos procesados de la API de MintITV
Utiliza solo la biblioteca estándar de Python

Uso desde terminal:
    python3 list_processes.py --token <token> [opciones]
    python3 list_processes.py --help
"""

import json
import urllib.request
import urllib.error
import sys
import argparse
from typing import Dict, List, Any, TypedDict, Optional, Literal
from datetime import datetime


# Tipos de la API
class ProcessLicense(TypedDict):
    """Información de licencia en listado"""
    id: str
    code: str
    customerId: str
    itv: str


class ProcessTechnicalCard(TypedDict):
    """Ficha técnica resumida en listado"""
    type: Literal['coc', 'titv-old', 'titv-new', 'reduced', 'single-approval', 'cdc', 'manual']
    category: Literal['M1', 'M3', 'N1', 'N3', 'L', 'O', 'T', 'TR', 'OS', 'OSR']
    model: str


class ProcessDocumentItem(TypedDict):
    """Elemento de documento procesado en listado"""
    id: str
    license: ProcessLicense
    technicalCard: ProcessTechnicalCard
    status: Literal['PENDING', 'STRAIGHTENING', 'RECOGNIZING', 'COMPLETED', 'FAILED', 'RETRIEVED', 'ABORTED']
    documentId: Optional[str]
    createdAt: str
    updatedAt: str


class ListProcessResponse(TypedDict):
    """Respuesta del endpoint de listado"""
    processDocuments: List[ProcessDocumentItem]


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


def listar_documentos_procesados(token: str) -> List[ProcessDocumentItem]:
    """
    Listar todos los documentos procesados y en proceso
    
    Args:
        token: Token de autenticación JWT
        
    Returns:
        List[ProcessDocumentItem]: Lista de documentos procesados con tipos completos
            
    Raises:
        Exception: Si falla la petición
    """
    url = "https://rest.mintitv.com/api/v1/process"
    
    # Crear petición con autenticación
    cabeceras = crear_cabecera_bearer(token)
    peticion = urllib.request.Request(url, headers=cabeceras, method='GET')
    
    try:
        # Realizar petición
        with urllib.request.urlopen(peticion) as respuesta:
            # Leer y decodificar respuesta
            datos_respuesta = respuesta.read().decode('utf-8')
            json_respuesta = json.loads(datos_respuesta)
            
            # Extraer array de documentos procesados
            respuesta_tipada: ListProcessResponse = json_respuesta
            return respuesta_tipada.get('processDocuments', [])
            
    except urllib.error.HTTPError as e:
        # Manejar errores HTTP
        cuerpo_error = e.read().decode('utf-8')
        try:
            json_error = json.loads(cuerpo_error)
            mensaje_error = json_error.get('detail', f'HTTP {e.code}')
        except json.JSONDecodeError:
            mensaje_error = f"HTTP {e.code}: {e.reason}"
        
        raise Exception(f"Fallo al listar documentos: {mensaje_error}")
    except Exception as e:
        raise Exception(f"Error en la petición: {str(e)}")


def filtrar_por_estado(documentos: List[ProcessDocumentItem], estado: str) -> List[ProcessDocumentItem]:
    """
    Filtrar documentos por estado
    
    Args:
        documentos: Lista de documentos procesados
        estado: Estado por el cual filtrar (PENDING, STRAIGHTENING, RECOGNIZING, COMPLETED, FAILED, RETRIEVED, ABORTED)
        
    Returns:
        list: Documentos filtrados
    """
    return [doc for doc in documentos if doc.get('status') == estado]


def filtrar_por_tipo(documentos: List[ProcessDocumentItem], tipo_doc: str) -> List[ProcessDocumentItem]:
    """
    Filtrar documentos por tipo
    
    Args:
        documentos: Lista de documentos procesados
        tipo_doc: Tipo de documento (coc, titv-old, titv-new, reduced, single-approval, cdc)
        
    Returns:
        list: Documentos filtrados
    """
    return [doc for doc in documentos if doc.get('technicalCard', {}).get('type') == tipo_doc]


def filtrar_por_categoria(documentos: List[ProcessDocumentItem], categoria: str) -> List[ProcessDocumentItem]:
    """
    Filtrar documentos por categoría de vehículo
    
    Args:
        documentos: Lista de documentos procesados
        categoria: Categoría del vehículo (M1, M3, N1, N3, L, O, T, TR, OS, OSR)
        
    Returns:
        list: Documentos filtrados
    """
    return [doc for doc in documentos if doc.get('technicalCard', {}).get('category') == categoria]


def filtrar_por_itv(documentos: List[ProcessDocumentItem], itv: str) -> List[ProcessDocumentItem]:
    """
    Filtrar documentos por estación ITV
    
    Args:
        documentos: Lista de documentos procesados
        itv: Código de estación ITV
        
    Returns:
        list: Documentos filtrados
    """
    return [doc for doc in documentos if doc.get('license', {}).get('itv') == itv]


def ordenar_por_fecha(documentos: List[ProcessDocumentItem], campo: str = 'createdAt', inverso: bool = True) -> List[ProcessDocumentItem]:
    """
    Ordenar documentos por fecha
    
    Args:
        documentos: Lista de documentos procesados
        campo: Campo de fecha para ordenar ('createdAt' o 'updatedAt')
        inverso: True para más reciente primero, False para más antiguo primero
        
    Returns:
        list: Documentos ordenados
    """
    return sorted(
        documentos, 
        key=lambda x: datetime.fromisoformat(x.get(campo, '').replace('Z', '+00:00')),
        reverse=inverso
    )


def imprimir_resumen(documentos: List[ProcessDocumentItem]) -> None:
    """
    Imprimir estadísticas resumidas de documentos procesados
    
    Args:
        documentos: Lista de documentos procesados
    """
    print(f"\n=== Resumen de Documentos Procesados ===")
    print(f"Total de documentos: {len(documentos)}")
    
    # Contar por estado
    conteos_estado = {}
    for doc in documentos:
        estado = doc.get('status', 'DESCONOCIDO')
        conteos_estado[estado] = conteos_estado.get(estado, 0) + 1
    
    print("\nPor Estado:")
    for estado, conteo in sorted(conteos_estado.items()):
        print(f"  {estado}: {conteo}")
    
    # Contar por tipo
    conteos_tipo = {}
    for doc in documentos:
        tipo_doc = doc.get('technicalCard', {}).get('type', 'DESCONOCIDO')
        conteos_tipo[tipo_doc] = conteos_tipo.get(tipo_doc, 0) + 1
    
    print("\nPor Tipo de Documento:")
    for tipo_doc, conteo in sorted(conteos_tipo.items()):
        print(f"  {tipo_doc}: {conteo}")
    
    # Contar por ITV
    conteos_itv = {}
    for doc in documentos:
        itv = doc.get('license', {}).get('itv', 'DESCONOCIDO')
        conteos_itv[itv] = conteos_itv.get(itv, 0) + 1
    
    print("\nPor Estación ITV:")
    for itv, conteo in sorted(conteos_itv.items()):
        print(f"  {itv}: {conteo}")


def imprimir_lista_documentos(documentos: List[ProcessDocumentItem], limite: int = 10) -> None:
    """
    Imprimir lista de documentos con información básica
    
    Args:
        documentos: Lista de documentos procesados
        limite: Número máximo a mostrar
    """
    print(f"\n=== Documentos Procesados (mostrando {min(len(documentos), limite)} de {len(documentos)}) ===")
    
    for i, doc in enumerate(documentos[:limite]):
        print(f"\n{i+1}. ID: {doc.get('id')}")
        print(f"   Estado: {doc.get('status')}")
        print(f"   Tipo: {doc.get('technicalCard', {}).get('type')}")
        print(f"   Categoría: {doc.get('technicalCard', {}).get('category')}")
        print(f"   Modelo: {doc.get('technicalCard', {}).get('model', 'N/D')}")
        print(f"   ITV: {doc.get('license', {}).get('itv')}")
        print(f"   Creado: {doc.get('createdAt')}")


def main():
    """Función principal para ejecución desde línea de comandos"""
    parser = argparse.ArgumentParser(
        description='Listar documentos procesados de la API de MintITV',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Ejemplos:
    # Listar todos los documentos:
    python3 list_processes.py --token "$token"
    
    # Filtrar por estado:
    python3 list_processes.py --token "$token" --estado COMPLETED
    
    # Filtrar por tipo y categoría:
    python3 list_processes.py --token "$token" --tipo coc --categoria M1
    
    # Mostrar solo resumen:
    python3 list_processes.py --token "$token" --resumen
    
    # Guardar en JSON:
    python3 list_processes.py --token "$token" --json documentos.json
''')
    
    parser.add_argument('--token', '-t', required=True,
                       help='Token JWT de autenticación (obtenido con login.py)')
    
    # Filtros
    parser.add_argument('--estado', '-e', 
                       choices=['PENDING', 'STRAIGHTENING', 'RECOGNIZING', 
                               'COMPLETED', 'FAILED', 'RETRIEVED', 'ABORTED'],
                       help='Filtrar por estado del proceso')
    parser.add_argument('--tipo', '-tp',
                       choices=['coc', 'titv-old', 'titv-new', 'reduced', 
                               'single-approval', 'cdc'],
                       help='Filtrar por tipo de documento')
    parser.add_argument('--categoria', '-c',
                       choices=['M1', 'M3', 'N1', 'N3', 'L', 'O', 'T', 'TR', 'OS', 'OSR'],
                       help='Filtrar por categoría de vehículo')
    parser.add_argument('--itv', '-i',
                       help='Filtrar por estación ITV')
    
    # Opciones de visualización
    parser.add_argument('--limite', '-l', type=int, default=10,
                       help='Número máximo de documentos a mostrar (default: 10)')
    parser.add_argument('--resumen', '-r', action='store_true',
                       help='Mostrar solo resumen estadístico')
    parser.add_argument('--json', '-j', metavar='ARCHIVO',
                       help='Guardar resultado completo en archivo JSON')
    parser.add_argument('--orden', '-o', choices=['fecha-asc', 'fecha-desc'],
                       default='fecha-desc',
                       help='Ordenar por fecha (default: fecha-desc)')
    parser.add_argument('-v', '--verbose', action='store_true',
                       help='Mostrar información detallada')
    
    args = parser.parse_args()
    
    try:
        # Obtener todos los documentos
        if args.verbose:
            print("Obteniendo documentos procesados...")
        
        documentos = listar_documentos_procesados(args.token)
        
        if args.verbose:
            print(f"Total de documentos obtenidos: {len(documentos)}")
        
        # Aplicar filtros
        if args.estado:
            documentos = filtrar_por_estado(documentos, args.estado)
            if args.verbose:
                print(f"Filtrado por estado '{args.estado}': {len(documentos)} documentos")
        
        if args.tipo:
            documentos = filtrar_por_tipo(documentos, args.tipo)
            if args.verbose:
                print(f"Filtrado por tipo '{args.tipo}': {len(documentos)} documentos")
        
        if args.categoria:
            documentos = filtrar_por_categoria(documentos, args.categoria)
            if args.verbose:
                print(f"Filtrado por categoría '{args.categoria}': {len(documentos)} documentos")
        
        if args.itv:
            documentos = filtrar_por_itv(documentos, args.itv)
            if args.verbose:
                print(f"Filtrado por ITV '{args.itv}': {len(documentos)} documentos")
        
        # Ordenar
        inverso = args.orden == 'fecha-desc'
        documentos = ordenar_por_fecha(documentos, inverso=inverso)
        
        # Mostrar resultados
        if args.resumen:
            imprimir_resumen(documentos)
        else:
            if len(documentos) == 0:
                print("No se encontraron documentos con los filtros especificados.")
            else:
                imprimir_lista_documentos(documentos, limite=args.limite)
                if len(documentos) > args.limite:
                    print(f"\n(Mostrando {args.limite} de {len(documentos)} documentos)")
        
        # Guardar en archivo si se especificó
        if args.json:
            with open(args.json, 'w', encoding='utf-8') as f:
                json.dump(documentos, f, indent=2, ensure_ascii=False)
            if args.verbose:
                print(f"\nResultados guardados en: {args.json}")
        
        return 0
        
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return 1


# Ejecutar si es el script principal
if __name__ == "__main__":
    sys.exit(main())