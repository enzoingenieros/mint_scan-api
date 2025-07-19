# MintITV CLI

[![Java Version](https://img.shields.io/badge/Java-11%2B-orange)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-brightgreen)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Aplicación de línea de comandos (CLI) para interactuar con la API de MintITV. Proporciona todas las funcionalidades necesarias para autenticación, listado, recuperación y procesamiento de documentos técnicos de vehículos.

## 🚀 Características

- ✅ **Autenticación JWT** con soporte para entrada interactiva de contraseñas
- 📋 **Listado de documentos** con filtros avanzados y estadísticas
- 🔍 **Recuperación de documentos** específicos con todos los detalles técnicos
- 🖼️ **Procesamiento de imágenes** individuales o múltiples (PDF, JPG, PNG, TIFF)
- 🐳 **Docker ready** - No requiere Java instalado
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

## 🛠️ Instalación

### Método 1: Compilación con Java/Maven

```bash
# Clonar o descargar el proyecto
cd java/

# Compilar
mvn clean package

# Ejecutar
java -jar target/mintitv-cli.jar help
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
mintitv-cli help
```

## 📖 Uso Básico

> 📚 **Nota**: Para una guía detallada del CLI, consulta [CLI_README.md](CLI_README.md)

### Ayuda

```bash
# Ayuda general
mintitv-cli help

# Ayuda específica de comando
mintitv-cli login --help
mintitv-cli help process
```

### 1. Autenticación

```bash
# Login interactivo (recomendado)
mintitv-cli login miusuario
# Contraseña: [entrada oculta]

# Login con contraseña (menos seguro)
mintitv-cli login usuario@ejemplo.com micontraseña

# Usando variables de entorno
export MINTITV_USER=miusuario
export MINTITV_PASS=micontraseña
mintitv-cli login

# Obtener solo el token (útil para scripts)
export MINTITV_TOKEN=$(mintitv-cli login -q)
```

### 2. Listar Documentos

```bash
# Configurar token
export MINTITV_TOKEN="tu-token-jwt"

# Listar todos
mintitv-cli list

# Filtrar por estado
mintitv-cli list --estado COMPLETED

# Filtrar por tipo y categoría
mintitv-cli list --tipo coc --categoria M1

# Mostrar resumen estadístico
mintitv-cli list --resumen

# Guardar en JSON
mintitv-cli list --json documentos.json

# Limitar resultados
mintitv-cli list --limite 20
```

### 3. Recuperar Documento

```bash
# Recuperar por ID
mintitv-cli retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422

# Solo resumen
mintitv-cli retrieve <id> --format resumen

# Exportar a JSON
mintitv-cli retrieve <id> --json resultado.json
```

### 4. Procesar Imágenes

```bash
# Procesar un archivo
mintitv-cli process --tipo coc --categoria M1 documento.pdf

# Múltiples archivos
mintitv-cli process --tipo titv-new --categoria N1 frente.jpg reverso.jpg

# Con nombre descriptivo
mintitv-cli process --tipo coc --categoria M1 --nombre "BMW 2024" doc.pdf

# Especificar ID de proceso
mintitv-cli process --tipo coc --categoria M1 --id 731cb083-7d83-4ce7-a0ce-1a3b19b7e422 imagen.jpg
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
docker-compose run --rm mintitv-cli login
docker-compose run --rm mintitv-cli list

# Con variables de entorno
MINTITV_TOKEN="token" docker-compose run --rm mintitv-cli list
```

### Docker directo

```bash
# Construir imagen
docker build -t mintitv-cli:latest .

# Ejecutar
docker run --rm -it mintitv-cli:latest login usuario
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

# CI/CD
make test
make package
make release
```

## 🌍 Variables de Entorno

| Variable | Descripción |
|----------|-------------|
| `MINTITV_USER` | Usuario para autenticación automática |
| `MINTITV_PASS` | Contraseña para autenticación automática |
| `MINTITV_TOKEN` | Token JWT para evitar login |

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
├── src/main/java/com/mintitv/
│   ├── api/                    # Librería API
│   │   ├── auth/              # Servicios de autenticación
│   │   ├── process/           # Servicios de procesamiento
│   │   ├── models/            # Modelos de datos
│   │   ├── exceptions/        # Excepciones personalizadas
│   │   └── utils/             # Utilidades
│   └── cli/                   # Aplicación CLI
│       ├── MintItvCli.java    # Punto de entrada
│       ├── LoginCommand.java  # Comando login
│       ├── ListCommand.java   # Comando list
│       ├── RetrieveCommand.java # Comando retrieve
│       └── ProcessCommand.java  # Comando process
├── pom.xml                    # Configuración Maven
├── Dockerfile                 # Build multi-etapa
├── docker-compose.yml         # Orquestación
├── Makefile                   # Automatización
├── docker-build.sh           # Script de compilación Docker
└── docker-run.sh             # Script de ejecución Docker
```

## 🔍 Ejemplos Avanzados

### Flujo Completo

```bash
# 1. Autenticación y guardar token
export MINTITV_TOKEN=$(mintitv-cli login usuario contraseña -q)

# 2. Procesar un documento
mintitv-cli process --tipo coc --categoria M1 mi-documento.pdf

# 3. Esperar y verificar estado
mintitv-cli list --estado COMPLETED | grep "mi-documento"

# 4. Recuperar resultado
mintitv-cli retrieve 123e4567-e89b-12d3-a456-426614174000 --json resultado.json
```

### Script Bash de Automatización

```bash
#!/bin/bash
# proceso-batch.sh

# Login
TOKEN=$(mintitv-cli login $MINTITV_USER $MINTITV_PASS -q)
export MINTITV_TOKEN=$TOKEN

# Procesar múltiples documentos
for file in documentos/*.pdf; do
    echo "Procesando: $file"
    mintitv-cli process --tipo coc --categoria M1 "$file"
done

# Listar resultados
mintitv-cli list --estado COMPLETED --json resultados.json
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
    - export MINTITV_TOKEN=$(./docker-run.sh login $USER $PASS -q)
    - ./docker-run.sh process --tipo coc --categoria M1 document.pdf
```

## 🐛 Solución de Problemas

### Token expirado
```bash
# Regenerar token
export MINTITV_TOKEN=$(mintitv-cli login -q)
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
chmod +x docker-*.sh mintitv-cli
```

### Java no encontrado
```bash
# Usar Docker en su lugar
./docker-run.sh <comando>
# o
make docker-run ARGS="<comando>"
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

- 📧 Email: soporte@mintitv.com
- 🐛 Issues: [GitHub Issues](https://github.com/mintitv/cli/issues)
- 📖 Documentación API: [https://docs.mintitv.com](https://docs.mintitv.com)

## 🏆 Créditos

Desarrollado por el equipo de MintITV.

---

**Nota**: Este CLI es compatible con la API v1 de MintITV. Asegúrese de tener credenciales válidas antes de usar.