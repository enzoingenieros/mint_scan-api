# MintScan CLI

[![Java Version](https://img.shields.io/badge/Java-11%2B-orange)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-brightgreen)](https://www.docker.com/)
[![GraalVM Native](https://img.shields.io/badge/GraalVM-Native%20Image-purple)](https://www.graalvm.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Aplicación de línea de comandos (CLI) para interactuar con la API de MintScan. Proporciona todas las funcionalidades necesarias para autenticación, listado, recuperación y procesamiento de documentos técnicos de vehículos.

## 🚀 Características

- ✅ **Autenticación JWT** con soporte para entrada interactiva de contraseñas
- 📋 **Listado de documentos** con filtros avanzados y estadísticas
- 🔍 **Recuperación de documentos** específicos con todos los detalles técnicos
- 🖼️ **Procesamiento de imágenes** individuales o múltiples (PDF, JPG, PNG, TIFF)
- 🐳 **Docker ready** - No requiere Java instalado
- 🚀 **GraalVM Native Image** - Binario nativo sin JVM (inicio instantáneo)
- 🔧 **Makefile incluido** para automatización
- 🌍 **Variables de entorno** para configuración
- 🎨 **Salida con colores** y formato amigable

## 📋 Requisitos

### Opción 1: Java local
- Java 11 o superior
- Maven 3.6+ (para compilación)

### Opción 2: Docker
- Docker instalado
- No requiere Java ni Maven

### Opción 3: Binario nativo
- Ningún requisito (el binario es autocontenido)
- O GraalVM 22.3+ con native-image para compilar

## 🛠️ Instalación

### Método 1: Compilación con Java/Maven

```bash
# Clonar o descargar el proyecto
cd java/

# Compilar
mvn clean package

# Ejecutar
java -jar target/mint_scan-cli.jar help
```

### Método 2: Compilación con Docker (recomendado)

```bash
# Compilar usando Docker
./docker-build.sh

# O usando Make
make docker-build
```

### Método 3: Instalación en el sistema

```bash
# Instalar en ~/bin
make install

# Agregar al PATH
echo 'export PATH=$PATH:~/bin' >> ~/.bashrc
source ~/.bashrc

# Usar directamente
mint_scan-cli help
```

### Método 4: Binario Nativo con GraalVM

```bash
# Compilar binario nativo con Docker (no requiere GraalVM local)
make docker-native-build
# o
./docker-build.sh native

# Con GraalVM instalado localmente
make native-build

# El binario se genera en target/mintscan-cli
./target/mintscan-cli help

# Copiar a directorio del sistema
sudo cp target/mintscan-cli /usr/local/bin/
mintscan-cli help
```

## 📖 Uso Básico

> 📚 **Nota**: Para una guía detallada del CLI, consulta [CLI_README.md](CLI_README.md)

### Ayuda

```bash
# Ayuda general
mint_scan-cli help

# Ayuda específica de comando
mint_scan-cli login --help
mint_scan-cli help process
```

### 1. Autenticación

```bash
# Login interactivo (recomendado)
mint_scan-cli login miusuario
# Contraseña: [entrada oculta]

# Login con contraseña (menos seguro)
mint_scan-cli login usuario@ejemplo.com micontraseña

# Usando variables de entorno
export MINTSCAN_USER=miusuario
export MINTSCAN_PASS=micontraseña
mint_scan-cli login

# Obtener solo el token (útil para scripts)
export MINTSCAN_TOKEN=$(mint_scan-cli login -q)
```

### 2. Listar Documentos

```bash
# Configurar token
export MINTSCAN_TOKEN="tu-token-jwt"

# Listar todos
mint_scan-cli list

# Filtrar por estado
mint_scan-cli list --estado COMPLETED

# Filtrar por tipo y categoría
mint_scan-cli list --tipo coc --categoria M1

# Mostrar resumen estadístico
mint_scan-cli list --resumen

# Guardar en JSON
mint_scan-cli list --json documentos.json

# Limitar resultados
mint_scan-cli list --limite 20
```

### 3. Recuperar Documento

```bash
# Recuperar por ID
mint_scan-cli retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422

# Solo resumen
mint_scan-cli retrieve <id> --format resumen

# Exportar a JSON
mint_scan-cli retrieve <id> --json resultado.json
```

### 4. Procesar Imágenes

```bash
# Procesar un archivo
mint_scan-cli process --tipo coc --categoria M1 documento.pdf

# Múltiples archivos
mint_scan-cli process --tipo titv-new --categoria N1 frente.jpg reverso.jpg

# Con nombre descriptivo
mint_scan-cli process --tipo coc --categoria M1 --nombre "BMW 2024" doc.pdf

# Especificar ID de proceso
mint_scan-cli process --tipo coc --categoria M1 --id 731cb083-7d83-4ce7-a0ce-1a3b19b7e422 imagen.jpg
```

## 🐳 Uso con Docker

> 🐳 **Nota**: Para una guía completa de Docker, consulta [DOCKER_README.md](DOCKER_README.md)

### Script docker-run.sh

```bash
# Ejecutar cualquier comando sin Java instalado
./docker-run.sh login usuario
./docker-run.sh list
./docker-run.sh process --tipo coc --categoria M1 documento.pdf
```

### Docker Compose

```bash
# Ejecutar comandos
docker-compose run --rm mintscan-cli login
docker-compose run --rm mintscan-cli list

# Con variables de entorno
MINTSCAN_TOKEN="token" docker-compose run --rm mintscan-cli list
```

### Docker directo

```bash
# Construir imagen
docker build -t mintscan-cli:latest .

# Ejecutar
docker run --rm -it mintscan-cli:latest login usuario
```

## 🔧 Makefile

> 📘 **Nota**: Para instrucciones detalladas del Makefile, consulta [MAKEFILE_GUIDE.md](MAKEFILE_GUIDE.md)

El proyecto incluye un Makefile completo para automatización:

```bash
# Ver todos los comandos
make help

# Compilar (auto-detecta Java o Docker)
make build

# Comandos del CLI
make login USER=usuario PASS=contraseña
make list TOKEN=tu-token
make retrieve ID=documento-id TOKEN=tu-token
make process FILE=doc.pdf TYPE=coc CATEGORY=M1

# Docker
make docker-build
make docker-clean

# Binario nativo
make native-build        # Con GraalVM local
make docker-native-build # Con Docker

# CI/CD
make test
make package
make release
```

## 🌍 Variables de Entorno

| Variable | Descripción |
|----------|-------------|
| `MINTSCAN_USER` | Usuario para autenticación automática |
| `MINTSCAN_PASS` | Contraseña para autenticación automática |
| `MINTSCAN_TOKEN` | Token JWT para evitar login |

## 📊 Tipos y Categorías

### Tipos de Documento
- `coc` - Certificate of Conformity
- `titv-old` - Tarjeta ITV formato antiguo
- `titv-new` - Tarjeta ITV formato nuevo
- `reduced` - Ficha técnica reducida
- `single-approval` - Homologación individual
- `cdc` - Documento CDC

### Categorías de Vehículo
- `M1`, `M3` - Vehículos de pasajeros
- `N1`, `N3` - Vehículos comerciales
- `L` - Vehículos ligeros
- `O` - Remolques
- `T`, `TR` - Vehículos agrícolas
- `OS`, `OSR` - Vehículos especiales

### Estados de Procesamiento
- `PENDING` - Pendiente
- `STRAIGHTENING` - Enderezando imagen
- `RECOGNIZING` - Reconociendo texto
- `COMPLETED` - Completado
- `FAILED` - Fallido
- `RETRIEVED` - Recuperado
- `ABORTED` - Abortado

## 📁 Estructura del Proyecto

```
java/
├── src/main/java/com/mintscan/
│   ├── api/                    # Librería API
│   │   ├── auth/              # Servicios de autenticación
│   │   ├── process/           # Servicios de procesamiento
│   │   ├── models/            # Modelos de datos
│   │   ├── exceptions/        # Excepciones personalizadas
│   │   └── utils/             # Utilidades
│   └── cli/                   # Aplicación CLI
│       ├── MintScanCli.java   # Punto de entrada
│       ├── commands/          # Comandos refactorizados
│       └── ...               # Comandos legacy
├── src/main/resources/
│   └── META-INF/native-image/ # Configuración GraalVM
│       ├── reflect-config.json
│       ├── resource-config.json
│       └── native-image.properties
├── target/
│   ├── mint_scan-cli.jar      # JAR ejecutable
│   ├── mint_scan-cli-shaded.jar # JAR con dependencias
│   └── mintscan-cli           # Binario nativo (después de compilar)
├── pom.xml                    # Configuración Maven + GraalVM
├── Dockerfile                 # Build multi-etapa
├── docker-compose.yml         # Orquestación
├── Makefile                   # Automatización + targets nativos
├── docker-build.sh           # Script de compilación (soporta native)
└── docker-run.sh             # Script de ejecución Docker
```

## 🔍 Ejemplos Avanzados

### Flujo Completo

```bash
# 1. Autenticación y guardar token
export MINTSCAN_TOKEN=$(mint_scan-cli login usuario contraseña -q)

# 2. Procesar un documento
mint_scan-cli process --tipo coc --categoria M1 mi-documento.pdf

# 3. Esperar y verificar estado
mint_scan-cli list --estado COMPLETED | grep "mi-documento"

# 4. Recuperar resultado
mint_scan-cli retrieve 123e4567-e89b-12d3-a456-426614174000 --json resultado.json
```

### Script Bash de Automatización

```bash
#!/bin/bash
# proceso-batch.sh

# Login
TOKEN=$(mint_scan-cli login $MINTSCAN_USER $MINTSCAN_PASS -q)
export MINTSCAN_TOKEN=$TOKEN

# Procesar múltiples documentos
for file in documentos/*.pdf; do
    echo "Procesando: $file"
    mint_scan-cli process --tipo coc --categoria M1 "$file"
done

# Listar resultados
mint_scan-cli list --estado COMPLETED --json resultados.json
```

### Integración con CI/CD

```yaml
# .gitlab-ci.yml o .github/workflows/process.yml
process-documents:
  image: docker:latest
  services:
    - docker:dind
  script:
    - ./docker-build.sh
    - export MINTSCAN_TOKEN=$(./docker-run.sh login $USER $PASS -q)
    - ./docker-run.sh process --tipo coc --categoria M1 document.pdf
```

## 🚀 Comparación de Métodos de Ejecución

| Método | Tiempo de inicio | Memoria RAM | Requisitos | Tamaño |
|--------|-----------------|-------------|------------|---------|
| JAR con JVM | ~1-2 segundos | ~100-200MB | Java 11+ | ~2.4MB + JVM |
| Binario nativo | ~10-50ms | ~10-50MB | Ninguno | ~31MB |
| Docker | ~2-3 segundos | ~150-250MB | Docker | ~180MB imagen |

## 🐛 Solución de Problemas

### Token expirado
```bash
# Regenerar token
export MINTSCAN_TOKEN=$(mint_scan-cli login -q)
```

### Archivo no encontrado en Docker
```bash
# Asegurarse de que el archivo esté en el directorio actual
cp /ruta/al/archivo.pdf .
./docker-run.sh process --tipo coc --categoria M1 archivo.pdf
```

### Error de permisos
```bash
# Dar permisos ejecutables
chmod +x docker-*.sh mint_scan-cli target/mintscan-cli
```

### Java no encontrado
```bash
# Opción 1: Usar Docker
./docker-run.sh <comando>
# o
make docker-run ARGS="<comando>"

# Opción 2: Usar binario nativo
./target/mintscan-cli <comando>
```

### Error al compilar imagen nativa
```bash
# Verificar que tienes suficiente memoria (mínimo 4GB libres)
free -h

# Usar Docker que gestiona la memoria automáticamente
make docker-native-build
```

## 🤝 Contribuir

1. Fork el proyecto
2. Crear rama de feature (`git checkout -b feature/nueva-caracteristica`)
3. Commit cambios (`git commit -am 'Agregar nueva característica'`)
4. Push a la rama (`git push origin feature/nueva-caracteristica`)
5. Crear Pull Request

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 📚 Documentación Adicional

- [CLI_README.md](CLI_README.md) - Guía detallada del CLI con ejemplos avanzados
- [DOCKER_README.md](DOCKER_README.md) - Guía completa para compilación y ejecución con Docker
- [MAKEFILE_GUIDE.md](MAKEFILE_GUIDE.md) - Instrucciones detalladas del Makefile y automatización

## 🆘 Soporte

- 📧 Email: soporte@mintscan.com
- 🐛 Issues: [GitHub Issues](https://github.com/mintscan/cli/issues)
- 📖 Documentación API: [https://docs.mintscan.com](https://docs.mintscan.com)

## 🏆 Créditos

Desarrollado por el equipo de MintScan.

## 📊 Detalles Técnicos del Binario Nativo

### Ventajas del Binario Nativo
- **Inicio instantáneo**: ~10-50ms vs 1-2 segundos con JVM
- **Menor consumo de memoria**: ~10-50MB vs ~100-200MB
- **Sin dependencias**: No requiere Java instalado
- **Distribución simple**: Un único archivo ejecutable

### Configuración GraalVM
El proyecto incluye configuración optimizada para GraalVM:
- Reflexión configurada para todos los modelos de datos
- Soporte completo para Jackson (serialización JSON)
- Protocolos HTTP/HTTPS habilitados
- Manejo correcto de fechas con java.time

### Compilación
```bash
# Tiempo estimado: 1-2 minutos
# Memoria requerida: ~4-6GB durante compilación
# Tamaño final: ~31MB

make docker-native-build
```

---

**Nota**: Este CLI es compatible con la API v1 de MintScan. Asegúrese de tener credenciales válidas antes de usar.