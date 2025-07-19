# MintScan API

Sistema de procesamiento de documentos de vehículos mediante OCR y reconocimiento inteligente.

## ⚠️ Advertencias de Seguridad

### **NUNCA hardcodees credenciales en el código**

- No incluyas contraseñas, tokens o claves API directamente en los archivos
- Usa variables de entorno o solicitud interactiva para credenciales sensibles
- Revisa el archivo `.gitignore` antes de hacer commits

### **Buenas prácticas de seguridad recomendadas:**

- Almacena credenciales en variables de entorno (`MINTSCAN_USER`, `MINTSCAN_PASS`)
- Usa la entrada interactiva de contraseñas cuando sea posible
- No compartas tokens JWT - son temporales y personales
- Revisa siempre los logs antes de compartirlos - pueden contener información sensible

## Descripción

MintScan API es una solución completa para digitalizar y procesar documentación técnica de vehículos, incluyendo:

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
export MINTSCAN_USER="tu_usuario"
export MINTSCAN_PASS="tu_contraseña"

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

## Cliente Java / Java Client

El proyecto incluye una implementación completa en Java de la API de MintScan, proporcionando tanto una librería reutilizable como una aplicación CLI con todas las funcionalidades.

### Ubicación del Proyecto Java

```text
java/
├── src/main/java/com/mintscan/
│   ├── api/                    # Librería API Java
│   │   ├── auth/              # Servicios de autenticación
│   │   ├── process/           # Servicios de procesamiento
│   │   ├── models/            # Modelos de datos
│   │   ├── exceptions/        # Manejo de errores
│   │   └── utils/             # Utilidades HTTP y Base64
│   └── cli/                   # Aplicación CLI
│       └── MintScanCli.java    # Punto de entrada principal
├── pom.xml                    # Configuración Maven
├── Dockerfile                 # Build con Docker (no requiere Java)
├── Makefile                   # Automatización completa
└── README.md                  # Documentación detallada
```

### Características del Cliente Java

- ☕ **Java 11+** con HttpClient nativo (sin dependencias pesadas)
- 📦 **Librería reutilizable** para integrar en otros proyectos Java
- 🖥️ **CLI completo** con todos los comandos en un solo ejecutable
- 🐳 **Docker ready** - Compila y ejecuta sin tener Java instalado
- 🔧 **Maven** para gestión de dependencias
- 🎯 **Type-safe** con modelos fuertemente tipados

### Uso Rápido del CLI Java

```bash
# Opción 1: Con Java instalado
cd java/
mvn clean package
java -jar target/mint_scan-cli.jar help

# Opción 2: Con Docker (sin Java)
cd java/
./docker-build.sh
./docker-run.sh help

# Opción 3: Con Make
cd java/
make build
make login USER=miusuario
make list
```

### Ejemplos de Uso

```bash
# Autenticación
java -jar target/mint_scan-cli.jar login miusuario
# O con Docker:
./docker-run.sh login miusuario

# Listar documentos
export MINTSCAN_TOKEN="tu-token"
java -jar target/mint_scan-cli.jar list --estado COMPLETED

# Procesar documento
java -jar target/mint_scan-cli.jar process --tipo coc --categoria M1 documento.pdf

# Recuperar resultado
java -jar target/mint_scan-cli.jar retrieve <id-documento>
```

### Uso como Librería Java

La librería se puede integrar en cualquier proyecto Java:

```java
// Autenticación
LoginService loginService = new LoginService();
String token = loginService.login("usuario", "contraseña");

// Listar documentos
ProcessListService listService = new ProcessListService(token);
ProcessListResponse response = listService.listDocuments(
    ProcessStatus.COMPLETED,  // estado
    DocumentType.COC,        // tipo
    VehicleCategory.M1       // categoría
);

// Procesar imagen
ProcessImagePoolService processService = new ProcessImagePoolService(token);
ProcessPoolResponse result = processService.processImage(
    DocumentType.COC,
    VehicleCategory.M1,
    "documento.pdf"
);
```

Para documentación completa del cliente Java, consulta [java/README.md](java/README.md).

## Cliente JavaScript/TypeScript

El proyecto también incluye una implementación completa en TypeScript que se ejecuta con Bun, proporcionando máximo rendimiento y tipos estrictos.

### Ubicación del Proyecto JavaScript

```text
javascript/
├── src/
│   ├── index.ts              # Punto de entrada principal
│   ├── commands/             # Implementación de comandos
│   ├── api/                  # Cliente HTTP y servicios
│   ├── models/               # Tipos TypeScript y constantes
│   ├── utils/                # Utilidades (validación, base64, etc.)
│   └── core/                 # Funcionalidad central
├── package.json
├── tsconfig.json
└── README.md                 # Documentación detallada
```

### Características del Cliente JavaScript

- 🚀 **Bun runtime** para máximo rendimiento
- 📝 **TypeScript** con tipos estrictos
- 🔐 **Autenticación JWT** con soporte para variables de entorno
- 🖥️ **CLI completo** compatible con scripts Python y Java
- 📊 **Exportación JSON** de resultados
- ⚡ **Sin dependencias pesadas** - solo lo esencial

### Uso Rápido

```bash
# Instalar Bun (si no está instalado)
curl -fsSL https://bun.sh/install | bash

# Instalar dependencias
cd javascript/
bun install

# Autenticación
bun run src/index.ts login usuario@ejemplo.com

# Procesar documento
export MINTSCAN_TOKEN="tu-token"
bun run src/index.ts process --tipo coc --categoria M1 documento.pdf

# Listar documentos
bun run src/index.ts list --estado COMPLETED

# Recuperar resultado
bun run src/index.ts retrieve <id-documento>
```

Para documentación completa del cliente JavaScript, consulta [javascript/README.md](javascript/README.md).

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

Para reportar problemas o solicitar características, contacta con el equipo de desarrollo de MintScan.
