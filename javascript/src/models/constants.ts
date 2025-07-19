// API Constants
export const API_BASE_URL = 'https://rest.mintitv.com/api/v1';

// Environment Variables
export const ENV_VARS = {
  USER: 'MINTSCAN_USER',
  PASS: 'MINTSCAN_PASS',
  TOKEN: 'MINTSCAN_TOKEN',
} as const;

// File Extensions and Types Mapping
export const FILE_TYPE_MAP: Record<string, string> = {
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.png': 'image/png',
  '.pdf': 'application/pdf',
  '.tiff': 'image/tiff',
  '.tif': 'image/tiff',
};

// Supported File Extensions
export const SUPPORTED_EXTENSIONS = Object.keys(FILE_TYPE_MAP);

// Max file size (30MB)
export const MAX_FILE_SIZE = 30 * 1024 * 1024;

// Document Types
export const DOCUMENT_TYPES = [
  'coc',
  'titv-old',
  'titv-new',
  'reduced',
  'single-approval',
  'cdc',
] as const;

// Vehicle Categories
export const VEHICLE_CATEGORIES = [
  'M1',
  'M3',
  'N1',
  'N3',
  'L',
  'O',
  'T',
  'TR',
  'OS',
  'OSR',
] as const;

// Process Statuses
export const PROCESS_STATUSES = [
  'PENDING',
  'STRAIGHTENING',
  'RECOGNIZING',
  'COMPLETED',
  'FAILED',
  'RETRIEVED',
  'ABORTED',
] as const;

// Field Descriptions (Spanish)
export const FIELD_DESCRIPTIONS: Record<string, string> = {
  // Sección A - Datos del fabricante
  'A.1': 'Nombre del fabricante del vehículo base',
  'A.2': 'Dirección del fabricante del vehículo base',
  
  // Sección B - Fechas y fabricante completado
  'B.1': 'Nombre del fabricante del vehículo completado',
  'B.2': 'Dirección del fabricante del vehículo completado',
  
  // Sección C - Códigos
  'C.I': 'Código ITV',
  'C.L': 'Clasificación del vehículo',
  'C.V': 'Control VIN',
  
  // Sección D - Datos del vehículo
  'D.1': 'Marca',
  'D.2': 'Tipo / Variante / Versión',
  'D.3': 'Denominación comercial del vehículo',
  'D.6': 'Procedencia',
  
  // Sección E - Identificación
  'E': 'Nº de identificación del vehículo',
  
  // Sección EP - Estructura de protección
  'EP': 'Estructura de protección',
  'EP.1': 'Marca de la estructura de protección',
  'EP.2': 'Modelo de la estructura de protección',
  'EP.3': 'N° de homologación de la estructura de protección',
  'EP.4': 'Nº identificativo de la estructura de protección',
  
  // Sección F - Masas y dimensiones
  'F.1': 'Masa Máxima en carga Técnicamente Admisible (MMTA)',
  'F.1.1': 'Masa Máxima en carga Técnicamente Admisible en cada eje 1°/2°/3°',
  'F.1.5': 'Masa Máxima en carga Técnicamente Admisible en 5a rueda o pivote de acoplamiento',
  'F.2': 'Masa Máxima en carga Admisible del Vehículo en circulación (MMA)',
  'F.2.1': 'Masa Máxima autorizada en cada eje 1°/2°/3°',
  'F.3': 'Masa Máxima Técnicamente Admisible del conjunto (MMTAC)',
  'F.3.1': 'Masa Máxima Autorizada del conjunto MMC',
  'F.4': 'Altura total',
  'F.5': 'Anchura total',
  'F.5.1': 'Anchura máxima carrozable',
  'F.6': 'Longitud total',
  'F.6.1': 'Longitud máxima carrozable',
  'F.7': 'Vía anterior',
  'F.7.1': 'Vía posterior',
  'F.8': 'Voladizo posterior',
  'F.8.1': 'Voladizo máximo posterior carrozable',
  
  // Sección G - Masas
  'G': 'Masa en Orden de marcha (MOM)',
  'G.1': 'Masa en vacío para vehículos categoría L',
  'G.2': 'Masa Mínima Admisible del vehículo completado',
  
  // Sección J - Categoría y carrocería
  'J': 'Categoría del vehículo',
  'J.1': 'Carrocería del vehículo',
  'J.2': 'Clase',
  'J.3': 'Volumen de bodegas',
  
  // Sección K - Homologación
  'K': 'N° de homologación del vehículo base',
  'K.1': 'N° de homologación del vehículo completado',
  'K.2': 'N° certificado TITV vehículo base',
  
  // Sección L - Ejes y ruedas
  'L': 'N° de ejes y ruedas',
  'L.0': 'Nº y posición de ejes con ruedas gemelas',
  'L.1': 'Ejes motrices',
  'L.2': 'Dimensiones de los neumáticos',
  
  // Sección M - Distancias
  'M.1': 'Distancia entre ejes 1°-2°, 2°-3°',
  'M.4': 'Distancia entre 5a rueda o pivote de acoplamiento y último eje',
  
  // Sección O - Remolque
  'O.1': 'Masa Remolcable con frenos / Masa Remolcable Técnicamente Admisible del vehículo de motor en caso de:',
  'O.1.1': 'Barra de tracción',
  'O.1.2': 'Semirremolque',
  'O.1.3': 'Remolque eje central',
  'O.1.4': 'Remolque sin freno',
  'O.2.1': 'Masa Máxima remolcable Técnicamente Admisible con frenos mecánicos',
  'O.2.2': 'Masa Máxima remolcable Técnicamente Admisible con frenos de inercia',
  'O.2.3': 'Masa Máxima remolcable Técnicamente Admisible con frenos hidráulicos o neumáticos',
  'O.3': 'Tipo de freno de servicio',
  
  // Sección P - Motor
  'P.1': 'Cilindrada',
  'P.1.1': 'Número y disposición de los cilindros',
  'P.2': 'Potencia de motor',
  'P.2.1': 'Potencia fiscal',
  'P.3': 'Tipo de combustible o fuente de energía',
  'P.5': 'Código de identificación del motor',
  'P.5.1': 'Fabricante o marca del motor',
  
  // Sección Q - Relación potencia/masa
  'Q': 'Relación potencia / masa',
  
  // Sección R - Color
  'R': 'Color',
  
  // Sección S - Plazas
  'S.1': 'Nº de plazas asiento / N° de asientos o sillines',
  'S.1.1': 'Nº de plazas de pie',
  'S.1.2': 'Cinturones de seguridad',
  'S.2': 'Nº de plazas de pie',
  
  // Sección T - Velocidad
  'T': 'Velocidad máxima',
  
  // Sección U - Ruido
  'U.1': 'Nivel sonoro en parada',
  'U.2': 'Velocidad del motor a la que se mide el nivel sonoro o vehículo parado',
  
  // Sección V - Emisiones
  'V.7': 'Emisiones de CO2',
  'V.8': 'Emisiones de CO',
  'V.9': 'Nivel de emisiones',
  
  // Sección Z - Año
  'Z': 'Año y número de la serie corta',
};