# Guía Rápida - Makefile MintScan CLI

## Instalación de Make

### Linux
```bash
# Debian/Ubuntu
sudo apt-get install make

# RHEL/CentOS/Fedora
sudo yum install make
```

### macOS
```bash
# Viene preinstalado, o instalar con Homebrew
brew install make
```

### Windows
```bash
# Opción 1: WSL (Windows Subsystem for Linux)
# Opción 2: Instalar con Chocolatey
choco install make

# Opción 3: MinGW o Cygwin
```

## Comandos Básicos

### Ver ayuda
```bash
make
# o
make help
```

### Compilar el proyecto
```bash
# Auto-detecta si usar Maven local o Docker
make build

# Forzar compilación con Docker (no requiere Java)
make docker-build

# Compilar todo (JAR + imagen Docker)
make build-all
```

### Limpiar
```bash
# Limpiar artefactos de compilación
make clean

# Limpiar imágenes Docker
make docker-clean
```

## Comandos del CLI

### Login
```bash
# Con usuario y contraseña
make login USER=miusuario PASS=micontraseña

# Solo usuario (pedirá contraseña)
make login USER=miusuario
```

### Listar documentos
```bash
# Con token como variable
make list TOKEN=tu-token-aqui

# Usando variable de entorno
export MINTSCAN_TOKEN=tu-token-aqui
make list

# Con filtros
make list ARGS="--estado COMPLETED --tipo coc"
```

### Recuperar documento
```bash
# Básico
make retrieve ID=123e4567-e89b-12d3-a456-426614174000 TOKEN=tu-token

# Con formato
make retrieve ID=123e4567-e89b-12d3-a456-426614174000 ARGS="--format resumen"
```

### Procesar imágenes
```bash
# Archivo único
make process FILE=documento.pdf TYPE=coc CATEGORY=M1

# Con nombre descriptivo
make process FILE=doc.pdf TYPE=coc CATEGORY=M1 ARGS="--nombre 'BMW 2024'"
```

## Flujos de Trabajo Completos

### Flujo con Java local
```bash
# 1. Verificar entorno
make dev-setup

# 2. Compilar
make build

# 3. Login y guardar token
export MINTSCAN_TOKEN=$(java -jar target/mint_scan-cli.jar login usuario contraseña -q)

# 4. Usar comandos
make list
make process FILE=mi-doc.pdf TYPE=coc CATEGORY=M1
```

### Flujo con Docker (sin Java)
```bash
# 1. Compilar con Docker
make docker-build

# 2. Login
export MINTSCAN_TOKEN=$(./docker-run.sh login usuario contraseña -q)

# 3. Usar comandos vía Docker
make docker-run ARGS="list"
make docker-run ARGS="process --tipo coc --categoria M1 doc.pdf"
```

### Flujo de CI/CD
```bash
# Pipeline completo
make ci

# Crear paquete de distribución
make package

# Release completo
make release
```

## Variables de Entorno

```bash
# Configurar credenciales
export MINTSCAN_USER=usuario
export MINTSCAN_PASS=contraseña
export MINTSCAN_TOKEN=token-jwt

# Los comandos las usarán automáticamente
make login      # Usará MINTSCAN_USER y MINTSCAN_PASS
make list       # Usará MINTSCAN_TOKEN
```

## Instalación en el Sistema

```bash
# Instalar en ~/bin
make install

# Agregar al PATH si no está
echo 'export PATH=$PATH:~/bin' >> ~/.bashrc
source ~/.bashrc

# Usar directamente
mint_scan-cli help
```

## Atajos Útiles

### Alias para comandos frecuentes
```bash
# En ~/.bashrc o ~/.zshrc
alias mlogin='make login USER=miusuario'
alias mlist='make list'
alias mclean='make clean docker-clean'
```

### Función para proceso rápido
```bash
# Agregar a ~/.bashrc
mintitv-process() {
    make process FILE="$1" TYPE="$2" CATEGORY="$3" "${@:4}"
}

# Usar
mintitv-process doc.pdf coc M1
```

## Troubleshooting

### Make no encontrado
```bash
# Verificar instalación
which make

# Instalar según tu sistema (ver arriba)
```

### Permisos denegados
```bash
# Dar permisos a scripts
chmod +x docker-*.sh mint_scan-cli
```

### Variables no definidas
```bash
# El Makefile mostrará qué variables faltan
make process
# Error: FILE variable required
# Usage: make process FILE=doc.pdf TYPE=coc CATEGORY=M1
```

### Docker no disponible
```bash
# El Makefile detecta automáticamente
# Si no hay Docker, intentará usar Java/Maven local
make build
```

## Personalización

### Cambiar imagen Docker
```makefile
# Editar Makefile
DOCKER_IMAGE := mi-registro/mint_scan-cli:latest
```

### Añadir comandos propios
```makefile
# Agregar al Makefile
mi-comando: ## Mi comando personalizado
	@echo "Ejecutando mi comando..."
	@$(MAKE) run ARGS="mi-logica"
```

## Comandos Avanzados

### Compilación paralela
```bash
# Si tienes Make 4.0+
make -j4 build test
```

### Modo verbose
```bash
# Ver comandos ejecutados
make build VERBOSE=1
```

### Targets específicos
```bash
# Solo ver qué se ejecutaría
make -n build

# Forzar reconstrucción
make -B build
```