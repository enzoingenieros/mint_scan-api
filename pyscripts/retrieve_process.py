#!/usr/bin/env python3
"""
Módulo para recuperar documentos procesados de la API de MintITV
Utiliza solo la biblioteca estándar de Python

Uso desde terminal:
    python3 retrieve_process.py --token <token> <id_proceso>
    python3 retrieve_process.py --help
"""

import json
import urllib.request
import urllib.error
import sys
import argparse
from typing import Dict, Any, TypedDict, Optional, List, Literal


# Diccionario de descripciones de campos de ficha técnica
FIELD_DESCRIPTIONS = {
    # Sección A - Datos del fabricante
    "A.1": "Nombre del fabricante del vehículo base",
    "A.2": "Dirección del fabricante del vehículo base",
    
    # Sección B - Fechas y fabricante completado
    "B.1": "Nombre del fabricante del vehículo completado",
    "B.2": "Dirección del fabricante del vehículo completado",
    
    # Sección C - Códigos
    "C.I": "Código ITV",
    "C.L": "Clasificación del vehículo",
    "C.V": "Control VIN",
    
    # Sección D - Datos del vehículo
    "D.1": "Marca",
    "D.2": "Tipo / Variante / Versión",
    "D.3": "Denominación comercial del vehículo",
    "D.6": "Procedencia",
    
    # Sección E - Identificación
    "E": "Nº de identificación del vehículo",
    
    # Sección EP - Estructura de protección
    "EP": "Estructura de protección",
    "EP.1": "Marca de la estructura de protección",
    "EP.2": "Modelo de la estructura de protección",
    "EP.3": "N° de homologación de la estructura de protección",
    "EP.4": "Nº identificativo de la estructura de protección",
    
    # Sección F - Masas y dimensiones
    "F.1": "Masa Máxima en carga Técnicamente Admisible (MMTA)",
    "F.1.1": "Masa Máxima en carga Técnicamente Admisible en cada eje 1°/2°/3°",
    "F.1.5": "Masa Máxima en carga Técnicamente Admisible en 5a rueda o pivote de acoplamiento",
    "F.2": "Masa Máxima en carga Admisible del Vehículo en circulación (MMA)",
    "F.2.1": "Masa Máxima autorizada en cada eje 1°/2°/3°",
    "F.3": "Masa Máxima Técnicamente Admisible del conjunto (MMTAC)",
    "F.3.1": "Masa Máxima Autorizada del conjunto MMC",
    "F.4": "Altura total",
    "F.5": "Anchura total",
    "F.5.1": "Anchura máxima carrozable",
    "F.6": "Longitud total",
    "F.6.1": "Longitud máxima carrozable",
    "F.7": "Vía anterior",
    "F.7.1": "Vía posterior",
    "F.8": "Voladizo posterior",
    "F.8.1": "Voladizo máximo posterior carrozable",
    
    # Sección G - Masas
    "G": "Masa en Orden de marcha (MOM)",
    "G.1": "Masa en vacío para vehículos categoría L",
    "G.2": "Masa Mínima Admisible del vehículo completado",
    
    # Sección J - Categoría y carrocería
    "J": "Categoría del vehículo",
    "J.1": "Carrocería del vehículo",
    "J.2": "Clase",
    "J.3": "Volumen de bodegas",
    
    # Sección K - Homologación
    "K": "N° de homologación del vehículo base",
    "Κ.1": "N° de homologación del vehículo completado",
    "K.1": "N° de homologación del vehículo completado",
    "K.2": "N° certificado TITV vehículo base",
    
    # Sección L - Ejes y ruedas
    "L": "N° de ejes y ruedas",
    "L.0": "Nº y posición de ejes con ruedas gemelas",
    "L.1": "Ejes motrices",
    "L.2": "Dimensiones de los neumáticos",
    
    # Sección M - Distancias
    "M.1": "Distancia entre ejes 1°-2°, 2°-3°",
    "M.4": "Distancia entre 5a rueda o pivote de acoplamiento y último eje",
    
    # Sección O - Remolque
    "O.1": "Masa Remolcable con frenos / Masa Remolcable Técnicamente Admisible del vehículo de motor en caso de:",
    "O.1.1": "Barra de tracción",
    "O.1.2": "Semirremolque",
    "O.1.3": "Remolque eje central",
    "O.1.4": "Remolque sin freno",
    "O.2.1": "Masa Máxima remolcable Técnicamente Admisible con frenos mecánicos",
    "O.2.2": "Masa Máxima remolcable Técnicamente Admisible con frenos de inercia",
    "O.2.3": "Masa Máxima remolcable Técnicamente Admisible con frenos hidráulicos o neumáticos",
    "O.3": "Tipo de freno de servicio",
    
    # Sección P - Motor
    "P.1": "Cilindrada",
    "P.1.1": "Número y disposición de los cilindros",
    "P.2": "Potencia de motor",
    "P.2.1": "Potencia fiscal",
    "P.3": "Tipo de combustible o fuente de energía",
    "P.5": "Código de identificación del motor",
    "P.5.1": "Fabricante o marca del motor",
    
    # Sección Q - Relación potencia/masa
    "Q": "Relación potencia / masa",
    
    # Sección R - Color
    "R": "Color",
    
    # Sección S - Plazas
    "S.1": "Nº de plazas asiento / N° de asientos o sillines",
    "S.1.1": "Nº de plazas de pie",
    "S.1.2": "Cinturones de seguridad",
    "S.2": "Nº de plazas de pie",
    
    # Sección T - Velocidad
    "T": "Velocidad máxima",
    
    # Sección U - Ruido
    "U.1": "Nivel sonoro en parada",
    "U.2": "Velocidad del motor a la que se mide el nivel sonoro o vehículo parado",
    
    # Sección V - Emisiones
    "V.7": "Emisiones de CO2",
    "V.8": "Emisiones de CO",
    "V.9": "Nivel de emisiones",
    
    # Sección Z - Año
    "Z": "Año y número de la serie corta"
}


# Tipos de la API
class License(TypedDict):
    """Información de licencia del vehículo"""
    id: str
    code: str
    customerId: str
    itv: str


# Usando la sintaxis alternativa de TypedDict para campos con caracteres especiales
TechnicalCardData = TypedDict('TechnicalCardData', {
    # Campos generales
    'matricula': Optional[str],
    'certificado': Optional[str],
    # Sección A - Datos del fabricante
    'A.1': Optional[str],  # Número de homologación CE
    'A.2': Optional[str],  # Tipo, variante, versión
    # Sección B - Fechas
    'B.1': Optional[str],  # Fecha de primera matriculación
    'B.2': Optional[str],  # Fecha de matriculación del vehículo
    # Sección D - Datos del vehículo
    'D.1': Optional[str],  # Marca
    'D.2': Optional[str],  # Tipo
    'D.3': Optional[str],  # Denominación comercial
    'D.6': Optional[str],  # Color del vehículo
    # Sección E - Identificación
    'E': Optional[str],    # Número de identificación del vehículo (VIN)
    # Sección F - Masas
    'F.1': Optional[str],  # MMA del vehículo en kg
    'F.1.1': Optional[str],  # MMTA del vehículo en kg
    'F.1.5': Optional[str],  # Masa del vehículo con carrocería en kg
    'F.2': Optional[str],  # MMA del vehículo en servicio en kg
    'F.2.1': Optional[str],  # MMTA del vehículo en servicio en kg
    'F.3': Optional[str],  # MCMA del conjunto en kg
    'F.3.1': Optional[str],  # MMCTA del conjunto en kg
    'F.4': Optional[str],  # MMA del remolque con freno en kg
    'F.5': Optional[str],  # MMA del remolque sin freno en kg
    'F.5.1': Optional[str],  # Carga vertical sobre el punto de acoplamiento en kg
    'F.6': Optional[str],  # Masa en orden de marcha del vehículo en kg
    'F.7': Optional[str],  # Masa máxima por eje en kg (eje 1)
    'F.7.1': Optional[str],  # Masa máxima por eje en kg (eje 2)
    'F.8': Optional[str],  # Masa máxima por eje en kg (eje 3)
    # Sección G - Masas máximas
    'G': Optional[str],    # Masa del vehículo en servicio
    'G.1': Optional[str],  # Peso máximo en carga
    # Sección J - Categoría del vehículo
    'J': Optional[str],    # Categoría del vehículo
    'J.1': Optional[str],  # Destino
    'J.2': Optional[str],  # Carrocería
    'J.3': Optional[str],  # Modalidad de carrocería
    # Sección K - Número de homologación de tipo
    'K': Optional[str],    # Número de homologación de tipo
    'K.1': Optional[str],  # Número de homologación de tipo (variante)
    'K.2': Optional[str],  # Número de homologación de tipo (versión)
    # Sección L - Número de ejes
    'L': Optional[str],    # Número de ejes
    'L.0': Optional[str],  # Distancia entre ejes en mm
    'L.1': Optional[str],  # Longitud del vehículo en mm
    'L.2': Optional[str],  # Anchura del vehículo en mm
    # Sección M - Distancia entre ejes
    'M.1': Optional[str],  # Distancia entre ejes 1-2 en mm
    'M.4': Optional[str],  # Altura del vehículo en mm
    # Sección O - Remolque
    'O.1': Optional[str],  # Remolque con freno en kg
    'O.1.1': Optional[str],  # Masa máxima remolcable del remolque con freno en kg
    'O.1.2': Optional[str],  # Masa máxima remolcable del remolque sin freno en kg
    'O.1.3': Optional[str],  # Masa máxima remolcable del eje central en kg
    'O.1.4': Optional[str],  # Masa máxima remolcable con freno del remolque en kg
    'O.2.1': Optional[str],  # Masa máxima del remolque en kg (eje 1)
    'O.2.2': Optional[str],  # Masa máxima del remolque en kg (eje 2)
    'O.2.3': Optional[str],  # Masa máxima del remolque en kg (eje 3)
    'O.3': Optional[str],  # Carga vertical máxima sobre el punto de acoplamiento en kg
    # Sección P - Motor
    'P.1': Optional[str],  # Cilindrada en cm3
    'P.1.1': Optional[str],  # Cilindrada del motor en litros
    'P.2': Optional[str],  # Potencia máxima en kW
    'P.2.1': Optional[str],  # Potencia máxima en CV
    'P.3': Optional[str],  # Tipo de combustible o fuente de energía
    'P.5': Optional[str],  # Denominación comercial del motor
    'P.5.1': Optional[str],  # Código del motor
    # Sección Q - Relación potencia/peso
    'Q': Optional[str],    # Relación potencia/peso en kW/kg
    # Sección R - Color
    'R': Optional[str],    # Color del vehículo
    # Sección S - Número de plazas
    'S.1': Optional[str],  # Número de plazas sentadas, incluido el conductor
    'S.1.1': Optional[str],  # Número de plazas de pie
    'S.2': Optional[str],  # Número de plazas de pie
    # Sección T - Velocidad máxima
    'T': Optional[str],    # Velocidad máxima en km/h
    # Sección U - Ruido
    'U.1': Optional[str],  # Nivel sonoro parado en dB(A)
    'U.2': Optional[str],  # Velocidad del motor en min-1
    # Sección V - Emisiones
    'V.7': Optional[str],  # CO2 (g/km)
    'V.8': Optional[str],  # Consumo de combustible (l/100km)
    'V.9': Optional[str],  # Indicación de la clase medioambiental de homologación CE
    # Sección Z - Observaciones
    'Z': Optional[str],    # Observaciones
    # Sección EP - Equipamiento especial
    'EP': Optional[str],   # Equipamiento especial
    'EP.1': Optional[str],  # Equipamiento especial 1
    'EP.2': Optional[str],  # Equipamiento especial 2
    'EP.3': Optional[str],  # Equipamiento especial 3
    'EP.4': Optional[str],  # Equipamiento especial 4
    # Campos adicionales
    'numEjes': Optional[str],
    'numRuedas': Optional[str],
    'numNeumaticos': Optional[str],
    'homologaciones': Optional[List[str]],
    'observaciones': Optional[List[str]],
    'reformas': Optional[List[str]],
    'fechaEmision': Optional[str]
})


class TechnicalCard(TypedDict):
    """Ficha técnica del vehículo"""
    type: Literal['coc', 'titv-old', 'titv-new', 'reduced', 'single-approval', 'cdc', 'manual']
    category: Literal['M1', 'M3', 'N1', 'N3', 'L', 'O', 'T', 'TR', 'OS', 'OSR']
    model: str
    vehicleLicense: Optional[str]
    vin: Optional[str]
    ict: Optional[bool]
    data: TechnicalCardData


class ProcessedDocument(TypedDict):
    """Documento procesado completo"""
    id: str
    documentId: Optional[str]
    license: License
    technicalCard: TechnicalCard
    status: Literal['PENDING', 'STRAIGHTENING', 'RECOGNIZING', 'COMPLETED', 'FAILED', 'RETRIEVED', 'ABORTED']
    createdAt: str
    updatedAt: str


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


def obtener_documento_procesado(token: str, id_proceso: str) -> ProcessedDocument:
    """
    Recuperar un documento procesado específico por ID
    
    Args:
        token: Token de autenticación JWT
        id_proceso: UUID del proceso a recuperar
        
    Returns:
        ProcessedDocument: Datos del documento procesado con tipos completos
            
    Raises:
        Exception: Si falla la petición o no se encuentra el documento
    """
    url = f"https://rest.mintitv.com/api/v1/process/{id_proceso}"
    
    # Crear petición con autenticación
    cabeceras = crear_cabecera_bearer(token)
    peticion = urllib.request.Request(url, headers=cabeceras, method='GET')
    
    try:
        # Realizar petición
        with urllib.request.urlopen(peticion) as respuesta:
            # Leer y decodificar respuesta
            datos_respuesta = respuesta.read().decode('utf-8')
            json_respuesta = json.loads(datos_respuesta)
            
            return json_respuesta
            
    except urllib.error.HTTPError as e:
        # Manejar errores HTTP sin exponer detalles del sistema
        cuerpo_error = e.read().decode('utf-8')
        try:
            json_error = json.loads(cuerpo_error)
            codigo_error = json_error.get('code', '')
            
            if e.code == 401:
                raise Exception("Token inválido o expirado")
            elif e.code == 403:
                raise Exception("Acceso denegado")
            elif e.code == 404:
                raise Exception("Servicio no disponible")
            elif e.code == 422:
                # Errores de lógica de negocio - mantener estos mensajes genéricos
                if codigo_error == 'DOCUMENT_NOT_FOUND':
                    raise Exception("Documento no encontrado")
                elif codigo_error == 'DOCUMENT_NOT_SAME_STATION':
                    raise Exception("Documento no disponible para esta estación")
                elif codigo_error == 'DOCUMENT_NOT_SAME_CUSTOMER':
                    raise Exception("Documento no disponible para este cliente")
                elif codigo_error == 'DOCUMENT_NOT_PROCESSED':
                    raise Exception("Documento aún no procesado")
                else:
                    raise Exception("Documento no disponible")
            elif e.code >= 500:
                raise Exception("Error del servidor")
            else:
                raise Exception(f"Error al recuperar documento (código: {e.code})")
                    
        except json.JSONDecodeError:
            if e.code >= 500:
                raise Exception("Error del servidor")
            else:
                raise Exception(f"Error de comunicación (código: {e.code})")
    except urllib.error.URLError:
        raise Exception("No se pudo conectar al servidor")
    except Exception as e:
        # Evitar mostrar detalles técnicos
        if "timed out" in str(e).lower():
            raise Exception("Tiempo de espera agotado")
        elif "connection" in str(e).lower():
            raise Exception("Error de conexión")
        else:
            raise Exception("Error al recuperar el documento")


def imprimir_detalles_proceso(datos_proceso: ProcessedDocument) -> None:
    """
    Imprimir TODOS los detalles del documento procesado de forma legible
    
    Args:
        datos_proceso: Datos del documento procesado desde la API
    """
    def obtener_descripcion_campo(codigo: str) -> str:
        """Obtener la descripción formateada del campo"""
        if codigo in FIELD_DESCRIPTIONS:
            return f"{FIELD_DESCRIPTIONS[codigo]} ({codigo})"
        else:
            # Si no hay descripción, mantener el formato original
            return codigo
    
    print(f"\n=== Detalles del Documento Procesado ===")
    print(f"ID: {datos_proceso.get('id')}")
    print(f"Estado: {datos_proceso.get('status')}")
    print(f"ID Documento: {datos_proceso.get('documentId', 'N/D')}")
    print(f"Creado: {datos_proceso.get('createdAt')}")
    print(f"Actualizado: {datos_proceso.get('updatedAt')}")
    
    # Información de licencia
    if 'license' in datos_proceso:
        info_licencia = datos_proceso['license']
        print(f"\n--- Información de Licencia ---")
        print(f"ITV: {info_licencia.get('itv')}")
        print(f"Código: {info_licencia.get('code')}")
        print(f"ID Cliente: {info_licencia.get('customerId')}")
    
    # Información de ficha técnica
    if 'technicalCard' in datos_proceso:
        ficha_tecnica = datos_proceso['technicalCard']
        print(f"\n--- Ficha Técnica ---")
        print(f"Tipo: {ficha_tecnica.get('type')}")
        print(f"Categoría: {ficha_tecnica.get('category')}")
        print(f"Modelo: {ficha_tecnica.get('model')}")
        print(f"Matrícula: {ficha_tecnica.get('vehicleLicense', 'N/D')}")
        print(f"VIN: {ficha_tecnica.get('vin', 'N/D')}")
        
        # TODOS los campos de datos
        if 'data' in ficha_tecnica:
            datos = ficha_tecnica['data']
            
            # Datos generales
            print(f"\n--- Datos Generales ---")
            print(f"Matrícula: {datos.get('matricula', 'N/D')}")
            print(f"Certificado: {datos.get('certificado', 'N/D')}")
            print(f"Fecha de emisión: {datos.get('fechaEmision', 'N/D')}")
            
            # Sección A - Datos del fabricante
            print(f"\n--- Sección A: Homologación ---")
            print(f"{obtener_descripcion_campo('A.1')}: {datos.get('A.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('A.2')}: {datos.get('A.2', 'N/D')}")
            
            # Sección B - Fechas
            print(f"\n--- Sección B: Fechas ---")
            print(f"{obtener_descripcion_campo('B.1')}: {datos.get('B.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('B.2')}: {datos.get('B.2', 'N/D')}")
            
            # Sección D - Datos del vehículo
            print(f"\n--- Sección D: Vehículo ---")
            print(f"{obtener_descripcion_campo('D.1')}: {datos.get('D.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('D.2')}: {datos.get('D.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('D.3')}: {datos.get('D.3', 'N/D')}")
            print(f"{obtener_descripcion_campo('D.6')}: {datos.get('D.6', 'N/D')}")
            
            # Sección E - Identificación
            print(f"\n--- Sección E: Identificación ---")
            print(f"{obtener_descripcion_campo('E')}: {datos.get('E', 'N/D')}")
            
            # Sección F - Masas
            print(f"\n--- Sección F: Masas (kg) ---")
            print(f"{obtener_descripcion_campo('F.1')}: {datos.get('F.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.1.1')}: {datos.get('F.1.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.1.5')}: {datos.get('F.1.5', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.2')}: {datos.get('F.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.2.1')}: {datos.get('F.2.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.3')}: {datos.get('F.3', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.3.1')}: {datos.get('F.3.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.4')}: {datos.get('F.4', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.5')}: {datos.get('F.5', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.5.1')}: {datos.get('F.5.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.6')}: {datos.get('F.6', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.7')}: {datos.get('F.7', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.7.1')}: {datos.get('F.7.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('F.8')}: {datos.get('F.8', 'N/D')}")
            
            # Sección G - Masas
            print(f"\n--- Sección G: Masa en servicio ---")
            print(f"{obtener_descripcion_campo('G')}: {datos.get('G', 'N/D')}")
            print(f"{obtener_descripcion_campo('G.1')}: {datos.get('G.1', 'N/D')}")
            
            # Sección J - Categoría
            print(f"\n--- Sección J: Categoría ---")
            print(f"{obtener_descripcion_campo('J')}: {datos.get('J', 'N/D')}")
            print(f"{obtener_descripcion_campo('J.1')}: {datos.get('J.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('J.2')}: {datos.get('J.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('J.3')}: {datos.get('J.3', 'N/D')}")
            
            # Sección K - Homologación
            print(f"\n--- Sección K: Homologación de tipo ---")
            print(f"{obtener_descripcion_campo('K')}: {datos.get('K', 'N/D')}")
            print(f"{obtener_descripcion_campo('K.1')}: {datos.get('K.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('K.2')}: {datos.get('K.2', 'N/D')}")
            
            # Sección L - Dimensiones
            print(f"\n--- Sección L: Dimensiones (mm) ---")
            print(f"{obtener_descripcion_campo('L')}: {datos.get('L', 'N/D')}")
            print(f"{obtener_descripcion_campo('L.0')}: {datos.get('L.0', 'N/D')}")
            print(f"{obtener_descripcion_campo('L.1')}: {datos.get('L.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('L.2')}: {datos.get('L.2', 'N/D')}")
            
            # Sección M - Distancias
            print(f"\n--- Sección M: Más dimensiones (mm) ---")
            print(f"{obtener_descripcion_campo('M.1')}: {datos.get('M.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('M.4')}: {datos.get('M.4', 'N/D')}")
            
            # Sección O - Remolque
            print(f"\n--- Sección O: Remolque (kg) ---")
            print(f"{obtener_descripcion_campo('O.1')}: {datos.get('O.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.1.1')}: {datos.get('O.1.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.1.2')}: {datos.get('O.1.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.1.3')}: {datos.get('O.1.3', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.1.4')}: {datos.get('O.1.4', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.2.1')}: {datos.get('O.2.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.2.2')}: {datos.get('O.2.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.2.3')}: {datos.get('O.2.3', 'N/D')}")
            print(f"{obtener_descripcion_campo('O.3')}: {datos.get('O.3', 'N/D')}")
            
            # Sección P - Motor
            print(f"\n--- Sección P: Motor ---")
            print(f"{obtener_descripcion_campo('P.1')}: {datos.get('P.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('P.1.1')}: {datos.get('P.1.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('P.2')}: {datos.get('P.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('P.2.1')}: {datos.get('P.2.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('P.3')}: {datos.get('P.3', 'N/D')}")
            print(f"{obtener_descripcion_campo('P.5')}: {datos.get('P.5', 'N/D')}")
            print(f"{obtener_descripcion_campo('P.5.1')}: {datos.get('P.5.1', 'N/D')}")
            
            # Sección Q - Relación potencia/peso
            print(f"\n--- Sección Q: Relación potencia/peso ---")
            print(f"{obtener_descripcion_campo('Q')}: {datos.get('Q', 'N/D')}")
            
            # Sección R - Color
            print(f"\n--- Sección R: Color ---")
            print(f"{obtener_descripcion_campo('R')}: {datos.get('R', 'N/D')}")
            
            # Sección S - Plazas
            print(f"\n--- Sección S: Plazas ---")
            print(f"{obtener_descripcion_campo('S.1')}: {datos.get('S.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('S.1.2')}: {datos.get('S.1.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('S.2')}: {datos.get('S.2', 'N/D')}")
            
            # Sección T - Velocidad
            print(f"\n--- Sección T: Velocidad ---")
            print(f"{obtener_descripcion_campo('T')}: {datos.get('T', 'N/D')}")
            
            # Sección U - Ruido
            print(f"\n--- Sección U: Ruido ---")
            print(f"{obtener_descripcion_campo('U.1')}: {datos.get('U.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('U.2')}: {datos.get('U.2', 'N/D')}")
            
            # Sección V - Emisiones
            print(f"\n--- Sección V: Emisiones ---")
            print(f"{obtener_descripcion_campo('V.7')}: {datos.get('V.7', 'N/D')}")
            print(f"{obtener_descripcion_campo('V.8')}: {datos.get('V.8', 'N/D')}")
            print(f"{obtener_descripcion_campo('V.9')}: {datos.get('V.9', 'N/D')}")
            
            # Sección Z - Observaciones
            print(f"\n--- Sección Z: Observaciones ---")
            print(f"{obtener_descripcion_campo('Z')}: {datos.get('Z', 'N/D')}")
            
            # Sección EP - Equipamiento especial
            print(f"\n--- Sección EP: Equipamiento especial ---")
            print(f"{obtener_descripcion_campo('EP')}: {datos.get('EP', 'N/D')}")
            print(f"{obtener_descripcion_campo('EP.1')}: {datos.get('EP.1', 'N/D')}")
            print(f"{obtener_descripcion_campo('EP.2')}: {datos.get('EP.2', 'N/D')}")
            print(f"{obtener_descripcion_campo('EP.3')}: {datos.get('EP.3', 'N/D')}")
            print(f"{obtener_descripcion_campo('EP.4')}: {datos.get('EP.4', 'N/D')}")
            
            # Otros campos
            print(f"\n--- Otros datos ---")
            print(f"Número de ejes: {datos.get('numEjes', 'N/D')}")
            print(f"Número de ruedas: {datos.get('numRuedas', 'N/D')}")
            print(f"Número de neumáticos: {datos.get('numNeumaticos', 'N/D')}")
            print(f"ICT: {ficha_tecnica.get('ict', 'N/D')}")
            
            # Arrays
            if datos.get('homologaciones'):
                print(f"\n--- Homologaciones ---")
                for i, h in enumerate(datos['homologaciones'], 1):
                    print(f"  {i}. {h}")
            
            if datos.get('observaciones'):
                print(f"\n--- Observaciones adicionales ---")
                for i, o in enumerate(datos['observaciones'], 1):
                    print(f"  {i}. {o}")
            
            if datos.get('reformas'):
                print(f"\n--- Reformas ---")
                for i, r in enumerate(datos['reformas'], 1):
                    print(f"  {i}. {r}")


def main():
    """Función principal para ejecución desde línea de comandos"""
    parser = argparse.ArgumentParser(
        description='Recuperar documento procesado de la API de MintITV',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Ejemplos:
    # Primero obtener token con login.py:
    token=$(python3 login.py usuario clave -q)
    
    # Luego recuperar documento:
    python3 retrieve_process.py --token "$token" 731cb083-7d83-4ce7-a0ce-1a3b19b7e422
    
    # Guardar resultado en archivo JSON:
    python3 retrieve_process.py --token "$token" <id> --json resultado.json
''')
    
    parser.add_argument('id_proceso', help='UUID del proceso a recuperar')
    parser.add_argument('--token', '-t', required=True,
                       help='Token JWT de autenticación (obtenido con login.py)')
    parser.add_argument('--json', '-j', metavar='ARCHIVO',
                       help='Guardar resultado completo en archivo JSON')
    parser.add_argument('--format', '-f', choices=['completo', 'resumen', 'json-raw'],
                       default='completo',
                       help='Formato de salida (default: completo)')
    parser.add_argument('-v', '--verbose', action='store_true',
                       help='Mostrar información detallada')
    
    args = parser.parse_args()
    
    try:
        # Recuperar documento
        if args.verbose:
            print(f"Recuperando documento: {args.id_proceso}")
        
        datos_proceso = obtener_documento_procesado(args.token, args.id_proceso)
        
        # Mostrar según formato seleccionado
        if args.format == 'json-raw':
            print(json.dumps(datos_proceso, indent=2, ensure_ascii=False))
        elif args.format == 'resumen':
            print(f"\nDocumento: {args.id_proceso}")
            print(f"Estado: {datos_proceso.get('status')}")
            if 'technicalCard' in datos_proceso:
                tc = datos_proceso['technicalCard']
                print(f"Tipo: {tc.get('type')}")
                print(f"Categoría: {tc.get('category')}")
                if 'data' in tc:
                    data = tc['data']
                    print(f"Matrícula: {data.get('matricula', 'N/D')}")
                    print(f"VIN: {data.get('E', 'N/D')}")
        else:
            # Formato completo
            imprimir_detalles_proceso(datos_proceso)
        
        # Guardar en archivo si se especificó
        if args.json:
            with open(args.json, 'w', encoding='utf-8') as f:
                json.dump(datos_proceso, f, indent=2, ensure_ascii=False)
            if args.verbose:
                print(f"\nResultado guardado en: {args.json}")
        
        return 0
        
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        return 1


# Ejecutar si es el script principal
if __name__ == "__main__":
    sys.exit(main())