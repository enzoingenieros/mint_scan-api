# MintITV Scan API

Sistema de procesamiento de documentos de vehículos mediante OCR y reconocimiento inteligente.

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

### Uso Rápido

```bash
# 1. Autenticación
token=$(python3 pyscripts/login.py usuario contraseña -q)

# 2. Procesar un documento
python3 pyscripts/process_image_pool.py --token "$token" --tipo coc --categoria M1 documento.pdf

# 3. Ver documentos procesados
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

## Soporte

Para reportar problemas o solicitar características, contacta con el equipo de desarrollo de MintITV.