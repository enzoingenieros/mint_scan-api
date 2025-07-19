# MintScan CLI

Aplicación de línea de comandos para interactuar con la API de MintScan. Proporciona todas las funciones de los scripts Python en un único ejecutable Java.

## Compilación

```bash
cd java
mvn clean package
```

Esto generará dos archivos JAR ejecutables en el directorio `target/`:
- `mint_scan-cli.jar` - JAR ejecutable con todas las dependencias incluidas
- `mint_scan-cli-shaded.jar` - Alternativa usando Maven Shade Plugin

## Instalación

### Opción 1: Usar el script wrapper

```bash
# Hacer el script ejecutable (solo la primera vez)
chmod +x mint_scan-cli

# Añadir al PATH (opcional)
export PATH=$PATH:/ruta/a/java
```

### Opción 2: Crear alias

```bash
alias mint_scan-cli='java -jar /ruta/completa/a/java/target/mint_scan-cli.jar'
```

## Uso

### Comandos disponibles

```bash
# Mostrar ayuda general
mint_scan-cli help

# Mostrar versión
mint_scan-cli version

# Ayuda específica de un comando
mint_scan-cli help <comando>
mint_scan-cli <comando> --help
```

### 1. Login - Autenticación

```bash
# Con usuario interactivo
mint_scan-cli login miusuario

# Con usuario y contraseña
mint_scan-cli login usuario@ejemplo.com micontraseña

# Usando variables de entorno
export MINTSCAN_USER=miusuario
export MINTSCAN_PASS=micontraseña
mint_scan-cli login

# Obtener solo el token (útil para scripts)
mint_scan-cli login -q > token.txt
export MINTSCAN_TOKEN=$(mint_scan-cli login -q)
```

### 2. List - Listar documentos procesados

```bash
# Configurar token
export MINTSCAN_TOKEN="tu-token-aqui"

# Listar todos los documentos
mint_scan-cli list

# Filtrar por estado
mint_scan-cli list --estado COMPLETED

# Filtrar por tipo y categoría
mint_scan-cli list --tipo coc --categoria M1

# Filtrar por estación ITV
mint_scan-cli list --itv ESTACION01

# Mostrar resumen estadístico
mint_scan-cli list --resumen

# Guardar resultados en JSON
mint_scan-cli list --json documentos.json

# Limitar número de resultados
mint_scan-cli list --limite 20

# Ordenar por fecha
mint_scan-cli list --orden fecha-asc
```

### 3. Retrieve - Recuperar documento específico

```bash
# Recuperar documento por ID
mint_scan-cli retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422

# Mostrar solo resumen
mint_scan-cli retrieve <id> --format resumen

# Obtener JSON sin formato
mint_scan-cli retrieve <id> --format json-raw

# Guardar en archivo
mint_scan-cli retrieve <id> --json resultado.json
```

### 4. Process - Procesar imágenes

```bash
# Procesar un archivo
mint_scan-cli process --tipo coc --categoria M1 documento.pdf

# Procesar múltiples archivos
mint_scan-cli process --tipo titv-new --categoria N1 frente.jpg reverso.jpg

# Con nombre descriptivo
mint_scan-cli process --tipo coc --categoria M1 --nombre "BMW 2024" doc.pdf

# Especificar ID del proceso
mint_scan-cli process --tipo coc --categoria M1 --id 731cb083-7d83-4ce7-a0ce-1a3b19b7e422 imagen.jpg

# Con cálculo de precisión (experimental)
mint_scan-cli process --tipo coc --categoria M1 --precision documento.pdf
```

## Variables de entorno

- `MINTSCAN_USER` - Usuario para autenticación automática
- `MINTSCAN_PASS` - Contraseña para autenticación automática
- `MINTSCAN_TOKEN` - Token JWT para evitar autenticación

## Opciones globales

- `-h, --help` - Mostrar ayuda
- `-v, --verbose` - Mostrar información detallada
- `-q, --quiet` - Modo silencioso (solo salida esencial)

## Tipos de documento soportados

- `coc` - Certificate of Conformity
- `titv-old` - Tarjeta ITV formato antiguo
- `titv-new` - Tarjeta ITV formato nuevo
- `reduced` - Ficha técnica reducida
- `single-approval` - Homologación individual
- `cdc` - Documento CDC

## Categorías de vehículo

- `M1`, `M3` - Vehículos de pasajeros
- `N1`, `N3` - Vehículos comerciales
- `L` - Vehículos ligeros
- `O` - Remolques
- `T`, `TR` - Vehículos agrícolas
- `OS`, `OSR` - Vehículos especiales

## Estados de procesamiento

- `PENDING` - Pendiente de procesar
- `STRAIGHTENING` - Enderezando imagen
- `RECOGNIZING` - Reconociendo texto
- `COMPLETED` - Completado exitosamente
- `FAILED` - Procesamiento fallido
- `RETRIEVED` - Documento recuperado
- `ABORTED` - Procesamiento abortado

## Ejemplos de flujo completo

```bash
# 1. Autenticación
export MINTSCAN_TOKEN=$(mint_scan-cli login usuario contraseña -q)

# 2. Procesar documento
mint_scan-cli process --tipo coc --categoria M1 mi-documento.pdf

# 3. Listar documentos procesados
mint_scan-cli list --estado COMPLETED

# 4. Recuperar documento específico
mint_scan-cli retrieve 123e4567-e89b-12d3-a456-426614174000
```

## Diferencias con los scripts Python

- **Un solo ejecutable**: Todos los comandos están integrados en una sola aplicación
- **Sintaxis unificada**: Uso consistente de opciones y argumentos
- **Mejor manejo de errores**: Mensajes de error más descriptivos
- **Type-safe**: Validación de tipos en tiempo de compilación
- **Thread-safe**: Puede usarse en aplicaciones multi-hilo
- **Sin dependencias de Python**: Solo requiere Java 11+

## Requisitos

- Java 11 o superior
- Maven 3.6+ (solo para compilación)

## Solución de problemas

### El comando no se encuentra

```bash
# Verificar que el JAR existe
ls target/mint_scan-cli.jar

# Ejecutar directamente con Java
java -jar target/mint_scan-cli.jar <comando>
```

### Error de versión de Java

```bash
# Verificar versión de Java
java -version

# Debe ser 11 o superior
```

### Error de token expirado

```bash
# Generar nuevo token
export MINTSCAN_TOKEN=$(mint_scan-cli login -q)
```