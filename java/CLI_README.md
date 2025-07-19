# MintITV CLI

Aplicación de línea de comandos para interactuar con la API de MintITV. Proporciona todas las funciones de los scripts Python en un único ejecutable Java.

## Compilación

```bash
cd java
mvn clean package
```

Esto generará dos archivos JAR ejecutables en el directorio `target/`:
- `mintitv-cli.jar` - JAR ejecutable con todas las dependencias incluidas
- `mintitv-cli-shaded.jar` - Alternativa usando Maven Shade Plugin

## Instalación

### Opción 1: Usar el script wrapper

```bash
# Hacer el script ejecutable (solo la primera vez)
chmod +x mintitv-cli

# Añadir al PATH (opcional)
export PATH=$PATH:/ruta/a/java
```

### Opción 2: Crear alias

```bash
alias mintitv-cli='java -jar /ruta/completa/a/java/target/mintitv-cli.jar'
```

## Uso

### Comandos disponibles

```bash
# Mostrar ayuda general
mintitv-cli help

# Mostrar versión
mintitv-cli version

# Ayuda específica de un comando
mintitv-cli help <comando>
mintitv-cli <comando> --help
```

### 1. Login - Autenticación

```bash
# Con usuario interactivo
mintitv-cli login miusuario

# Con usuario y contraseña
mintitv-cli login usuario@ejemplo.com micontraseña

# Usando variables de entorno
export MINTITV_USER=miusuario
export MINTITV_PASS=micontraseña
mintitv-cli login

# Obtener solo el token (útil para scripts)
mintitv-cli login -q > token.txt
export MINTITV_TOKEN=$(mintitv-cli login -q)
```

### 2. List - Listar documentos procesados

```bash
# Configurar token
export MINTITV_TOKEN="tu-token-aqui"

# Listar todos los documentos
mintitv-cli list

# Filtrar por estado
mintitv-cli list --estado COMPLETED

# Filtrar por tipo y categoría
mintitv-cli list --tipo coc --categoria M1

# Filtrar por estación ITV
mintitv-cli list --itv ESTACION01

# Mostrar resumen estadístico
mintitv-cli list --resumen

# Guardar resultados en JSON
mintitv-cli list --json documentos.json

# Limitar número de resultados
mintitv-cli list --limite 20

# Ordenar por fecha
mintitv-cli list --orden fecha-asc
```

### 3. Retrieve - Recuperar documento específico

```bash
# Recuperar documento por ID
mintitv-cli retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422

# Mostrar solo resumen
mintitv-cli retrieve <id> --format resumen

# Obtener JSON sin formato
mintitv-cli retrieve <id> --format json-raw

# Guardar en archivo
mintitv-cli retrieve <id> --json resultado.json
```

### 4. Process - Procesar imágenes

```bash
# Procesar un archivo
mintitv-cli process --tipo coc --categoria M1 documento.pdf

# Procesar múltiples archivos
mintitv-cli process --tipo titv-new --categoria N1 frente.jpg reverso.jpg

# Con nombre descriptivo
mintitv-cli process --tipo coc --categoria M1 --nombre "BMW 2024" doc.pdf

# Especificar ID del proceso
mintitv-cli process --tipo coc --categoria M1 --id 731cb083-7d83-4ce7-a0ce-1a3b19b7e422 imagen.jpg

# Con cálculo de precisión (experimental)
mintitv-cli process --tipo coc --categoria M1 --precision documento.pdf
```

## Variables de entorno

- `MINTITV_USER` - Usuario para autenticación automática
- `MINTITV_PASS` - Contraseña para autenticación automática
- `MINTITV_TOKEN` - Token JWT para evitar autenticación

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
export MINTITV_TOKEN=$(mintitv-cli login usuario contraseña -q)

# 2. Procesar documento
mintitv-cli process --tipo coc --categoria M1 mi-documento.pdf

# 3. Listar documentos procesados
mintitv-cli list --estado COMPLETED

# 4. Recuperar documento específico
mintitv-cli retrieve 123e4567-e89b-12d3-a456-426614174000
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
ls target/mintitv-cli.jar

# Ejecutar directamente con Java
java -jar target/mintitv-cli.jar <comando>
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
export MINTITV_TOKEN=$(mintitv-cli login -q)
```