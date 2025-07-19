# MintITV CLI - Docker Build Guide

Esta guía explica cómo compilar y ejecutar el MintITV CLI usando Docker, sin necesidad de tener Java o Maven instalados en tu sistema.

## Requisitos

- Docker instalado ([Guía de instalación](https://docs.docker.com/get-docker/))
- Docker Compose (opcional, incluido con Docker Desktop)

## Compilación rápida

### Opción 1: Usar el script docker-build.sh

```bash
# Compilar solo el JAR
./docker-build.sh

# Compilar imagen Docker
./docker-build.sh image

# Compilar todo (JAR + imagen)
./docker-build.sh all

# Limpiar artefactos
./docker-build.sh clean
```

### Opción 2: Usar Docker Compose

```bash
# Compilar el proyecto
docker-compose run --rm builder

# O con el perfil de build
docker-compose --profile build run builder
```

### Opción 3: Comando Docker directo

```bash
# Compilar el JAR
docker run --rm -v $(pwd):/workspace -w /workspace maven:3.9.5-eclipse-temurin-11 mvn clean package

# Construir imagen Docker
docker build -t mintitv-cli:latest .
```

## Ejecución

### Método 1: Script docker-run.sh (Recomendado)

```bash
# Ver ayuda
./docker-run.sh help

# Login
./docker-run.sh login usuario contraseña

# Listar documentos
export MINTITV_TOKEN="tu-token"
./docker-run.sh list

# Procesar archivo (el archivo debe estar en el directorio actual)
./docker-run.sh process --tipo coc --categoria M1 documento.pdf
```

### Método 2: Docker Compose

```bash
# Ver ayuda
docker-compose run --rm mintitv-cli help

# Login interactivo
docker-compose run --rm mintitv-cli login usuario

# Con variables de entorno
MINTITV_TOKEN="tu-token" docker-compose run --rm mintitv-cli list
```

### Método 3: Docker directo

```bash
# Ejecutar con Docker
docker run --rm -it \
  -e MINTITV_TOKEN="$MINTITV_TOKEN" \
  -v $(pwd):/data \
  -w /data \
  mintitv-cli:latest list
```

## Estructura de archivos

```
java/
├── Dockerfile           # Construcción multi-etapa
├── docker-compose.yml   # Configuración de servicios
├── docker-build.sh      # Script de compilación
├── docker-run.sh        # Script de ejecución
├── .dockerignore        # Archivos a ignorar
└── ...
```

## Volúmenes y archivos

### Para el comando `process`

Los archivos a procesar deben estar en el directorio actual:

```bash
# Copiar archivo al directorio actual
cp /ruta/a/mi/documento.pdf .

# Procesar
./docker-run.sh process --tipo coc --categoria M1 documento.pdf
```

### Para exportar JSON

Los archivos se guardarán en el directorio actual:

```bash
# Listar y exportar
./docker-run.sh list --json documentos.json

# El archivo estará en ./documentos.json
```

## Variables de entorno

Las variables de entorno se pasan automáticamente:

```bash
export MINTITV_USER="usuario"
export MINTITV_PASS="contraseña"
export MINTITV_TOKEN="token"

# Se usarán automáticamente
./docker-run.sh login
./docker-run.sh list
```

## Solución de problemas

### Error: Docker no está instalado

```bash
# Verificar instalación
docker --version

# Si no está instalado, visitar:
# https://docs.docker.com/get-docker/
```

### Error: Docker daemon no está ejecutándose

```bash
# En Linux
sudo systemctl start docker

# En Mac/Windows
# Iniciar Docker Desktop
```

### Error: Permiso denegado

```bash
# Hacer scripts ejecutables
chmod +x docker-build.sh docker-run.sh

# En Linux, agregar usuario al grupo docker
sudo usermod -aG docker $USER
# Cerrar sesión y volver a entrar
```

### Error: Imagen no encontrada

```bash
# Construir la imagen
./docker-build.sh image

# O verificar imágenes disponibles
docker images | grep mintitv
```

### Archivos no encontrados

```bash
# Verificar que el archivo esté en el directorio actual
ls -la documento.pdf

# O usar ruta absoluta montando directorio padre
docker run --rm -v /ruta/completa:/data mintitv-cli:latest process ...
```

## Desarrollo

### Reconstruir después de cambios

```bash
# Limpiar y reconstruir
./docker-build.sh clean
./docker-build.sh all
```

### Usar caché de Maven

Docker Compose mantiene un volumen con el caché de Maven para acelerar compilaciones:

```bash
# Ver volúmenes
docker volume ls | grep maven

# Limpiar caché si es necesario
docker volume rm java_maven-cache
```

### Ejecutar con bash para debugging

```bash
# Entrar al contenedor
docker run --rm -it --entrypoint bash mintitv-cli:latest

# Dentro del contenedor
java -jar mintitv-cli.jar help
```

## Comparación de métodos

| Método | Ventajas | Desventajas |
|--------|----------|-------------|
| docker-build.sh | Simple, automatizado | Requiere script |
| docker-compose | Gestión de servicios, configuración persistente | Más complejo |
| Docker directo | Control total | Comandos largos |

## Ejemplos completos

### Flujo completo con Docker

```bash
# 1. Compilar el proyecto
./docker-build.sh all

# 2. Login
export MINTITV_TOKEN=$(./docker-run.sh login usuario contraseña -q)

# 3. Procesar documento
./docker-run.sh process --tipo coc --categoria M1 mi-documento.pdf

# 4. Verificar estado
./docker-run.sh list --estado COMPLETED

# 5. Recuperar resultado
./docker-run.sh retrieve <id-proceso> --json resultado.json
```

### Uso con Docker Compose

```bash
# 1. Compilar
docker-compose --profile build run builder

# 2. Configurar alias
alias mintitv='docker-compose run --rm mintitv-cli'

# 3. Usar como comando normal
mintitv login
mintitv list
mintitv process --tipo coc --categoria M1 doc.pdf
```

## Notas de seguridad

- Las contraseñas no se almacenan en la imagen Docker
- Use variables de entorno o entrada interactiva para credenciales
- Los archivos procesados permanecen en el host
- La imagen ejecuta como usuario no-root por seguridad