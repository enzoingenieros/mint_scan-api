# MintITV Scan API

Sistema de procesamiento de documentos de vehículos mediante OCR y reconocimiento inteligente.

## ⚠️ Advertencias de Seguridad

### **NUNCA hardcodees credenciales en el código**

- No incluyas contraseñas, tokens o claves API directamente en los archivos
- Usa variables de entorno o solicitud interactiva para credenciales sensibles
- Revisa el archivo `.gitignore` antes de hacer commits

### **Buenas prácticas de seguridad recomendadas:**

- Almacena credenciales en variables de entorno (`MINTITV_USER`, `MINTITV_PASS`)
- Usa la entrada interactiva de contraseñas cuando sea posible
- No compartas tokens JWT - son temporales y personales
- Revisa siempre los logs antes de compartirlos - pueden contener información sensible

## Descripción

MintITV Scan API es una solución completa para digitalizar y procesar documentación técnica de vehículos, incluyendo:

- Certificados de Conformidad (COC)
- Tarjetas ITV (antiguas y nuevas)
- Fichas técnicas reducidas
- Certificados de características
- Homologaciones individuales

El sistema utiliza tecnología OCR avanzada para extraer automáticamente información de los documentos y estructurarla en formato JSON.

## Características Principales

- 🚗 Procesamiento automático de documentos de vehículos
- 📄 Soporte para múltiples formatos: JPG, PNG, PDF, TIFF
- 🔍 Extracción inteligente de datos técnicos
- 🛡️ Autenticación JWT segura
- 📊 API RESTful completa
- 🔧 Scripts de línea de comandos incluidos

## Scripts de Cliente

El proyecto incluye un conjunto completo de scripts Python para interactuar con la API desde la línea de comandos. Estos scripts se encuentran en el directorio `pyscripts/`:

```text
pyscripts/
├── login.py              # Autenticación y obtención de token
├── process_image_pool.py # Procesamiento de documentos
├── list_processes.py     # Listado y filtrado de documentos
├── retrieve_process.py   # Recuperación de documentos procesados
└── README.md            # Documentación detallada de los scripts
```

### Uso Rápido (Seguro)

```bash
# 1. Configurar variables de entorno (recomendado)
export MINTITV_USER="tu_usuario"
export MINTITV_PASS="tu_contraseña"

# 2. Autenticación segura
# Opción A: Usando variables de entorno
token=$(python3 pyscripts/login.py -q)

# Opción B: Entrada interactiva (más seguro)
token=$(python3 pyscripts/login.py tu_usuario -q)  # Solicitará contraseña

# 3. Procesar un documento
python3 pyscripts/process_image_pool.py --token "$token" --tipo coc --categoria M1 documento.pdf

# 4. Ver documentos procesados
python3 pyscripts/list_processes.py --token "$token"

# 4. Obtener detalles de un documento
python3 pyscripts/retrieve_process.py --token "$token" <id-documento>
```

Para documentación completa sobre los scripts, consulta [pyscripts/README.md](pyscripts/README.md).

## Requisitos

### Para los scripts de cliente

- Python 3.6+
- Sin dependencias externas (solo biblioteca estándar)

### Para la API (servidor)

- Servidor con API de MintITV desplegada
- Credenciales de acceso válidas

## Tipos de Documentos Soportados

- **COC**: Certificado de Conformidad
- **Tarjeta ITV antigua**: Según RD 2140/1985
- **Tarjeta ITV nueva**: Según RD 750/2010
- **Ficha reducida**: Documentación técnica simplificada
- **Homologación individual**: Para vehículos únicos o modificados
- **Certificado de Características**: Documento técnico detallado

## Categorías de Vehículos

- **M1, M3**: Vehículos de pasajeros
- **N1, N3**: Vehículos comerciales
- **L**: Motocicletas y ciclomotores
- **O**: Remolques y semirremolques
- **T**: Tractores agrícolas
- **TR**: Tractores de carretera
- **OS, OSR**: Vehículos especiales

## Instalación Rápida

1. Clona el repositorio
2. Los scripts están listos para usar sin instalación adicional
3. Asegúrate de tener Python 3.6+ instalado

```bash
# Hacer ejecutables los scripts
chmod +x pyscripts/*.py

# Verificar funcionamiento
python3 pyscripts/login.py --help
```

## Documentación Adicional

- **Scripts de cliente**: Ver [pyscripts/README.md](pyscripts/README.md) para guía completa de uso
- **API REST**: Documentación disponible en el servidor de la API
- **Ejemplos**: Los scripts incluyen ayuda integrada con `--help`

## JSON Schema de la API

La API proporciona un JSON Schema completo que documenta todos los tipos de datos y estructuras utilizados. Puedes obtenerlo con:

```bash
# Obtener el JSON Schema completo
curl -L rest.mintitv.com/doc

# O guardarlo en un archivo
curl -L rest.mintitv.com/doc > api-schema.json
```

El schema incluye:

- Definiciones de todos los tipos de documentos (COC, Tarjetas ITV, etc.)
- Estructura completa de los datos de ficha técnica
- Códigos de error y respuestas de la API
- Validaciones y restricciones de campos

## Soporte

Para reportar problemas o solicitar características, contacta con el equipo de desarrollo de MintITV.
