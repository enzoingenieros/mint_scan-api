# Scripts Python para la API de MintITV

Scripts de línea de comandos en Python para interactuar con la API de MintITV.

## ⚠️ IMPORTANTE: Seguridad

**NUNCA** incluyas credenciales directamente en el código o comandos. Las credenciales en la línea de comandos quedan visibles en:

- Historial del terminal (`history`)
- Lista de procesos (`ps aux`)
- Logs del sistema

### Métodos seguros para manejar credenciales:

1. **Variables de entorno** (recomendado):

   ```bash
   export MINTITV_USER="usuario"
   export MINTITV_PASS="contraseña"
   python3 login.py
   ```

2. **Entrada interactiva**:

   ```bash
   python3 login.py usuario  # Solicitará la contraseña de forma segura
   ```

3. **Archivos de configuración** (no incluidos en control de versiones):
   - Crea un archivo `.env` con las credenciales
   - Asegúrate de que `.env` esté en `.gitignore`

## Requisitos

- Python 3.6+
- Sin dependencias externas (utiliza solo la biblioteca estándar)

## Instalación

No requiere instalación. Solo clona o descarga los scripts y ejecútalos directamente:

```bash
chmod +x *.py  # Hacer ejecutables los scripts
```

## Referencia Rápida

| Script                  | Función           | Comando básico                                                                              |
| ----------------------- | ----------------- | ------------------------------------------------------------------------------------------- |
| `login.py`              | Autenticación     | `python3 login.py <usuario> <contraseña>`                                                   |
| `retrieve_process.py`   | Obtener documento | `python3 retrieve_process.py --token <token> <id>`                                          |
| `list_processes.py`     | Listar documentos | `python3 list_processes.py --token <token>`                                                 |
| `process_image_pool.py` | Procesar imágenes | `python3 process_image_pool.py --token <token> --tipo <tipo> --categoria <cat> archivo.jpg` |

## Uso desde Terminal

### Flujo de trabajo seguro

```bash
# 1. Configurar credenciales de forma segura
export MINTITV_USER="tu_usuario"
export MINTITV_PASS="tu_contraseña"

# 2. Obtener token de autenticación
token=$(python3 login.py -q)

# 3. Procesar un documento
python3 process_image_pool.py --token "$token" --tipo coc --categoria M1 documento.pdf

# 4. Listar documentos procesados
python3 list_processes.py --token "$token"

# 4. Obtener detalles de un documento específico
python3 retrieve_process.py --token "$token" 731cb083-7d83-4ce7-a0ce-1a3b19b7e422
```

## Scripts Disponibles

**IMPORTANTE:** Todos los scripts son completamente independientes y no tienen dependencias entre ellos. Solo utilizan módulos nativos de Python 3.

### 1. `login.py` - Autenticación

Obtiene token JWT para acceder a la API.

#### Sintaxis:

```bash
python3 login.py <usuario> <contraseña> [opciones]
```

#### Opciones:

- `-q, --quiet`: Solo muestra el token (útil para scripts)
- `-v, --verbose`: Muestra información detallada
- `-h, --help`: Muestra ayuda

#### Ejemplos:

```bash
# Login seguro con entrada interactiva
python3 login.py usuario@ejemplo.com  # Solicitará contraseña

# Login usando variables de entorno (recomendado)
export MINTITV_USER="usuario@ejemplo.com"
export MINTITV_PASS="tu_contraseña_segura"
token=$(python3 login.py -q)

# Ver información detallada
python3 login.py -v
```

### 2. `retrieve_process.py` - Recuperar Documento Procesado

Obtiene detalles de un documento procesado específico por ID.

#### Sintaxis:

```bash
python3 retrieve_process.py --token <token> <id_proceso> [opciones]
```

#### Opciones:

- `--token, -t`: Token JWT (requerido)
- `--json, -j ARCHIVO`: Guardar resultado en archivo JSON
- `--format, -f`: Formato de salida: `completo`, `resumen`, `json-raw`
- `-v, --verbose`: Información detallada

#### Ejemplos:

```bash
# Obtener documento con formato completo
python3 retrieve_process.py --token "$token" 731cb083-7d83-4ce7-a0ce-1a3b19b7e422

# Solo resumen
python3 retrieve_process.py --token "$token" <id> --format resumen

# Guardar en JSON
python3 retrieve_process.py --token "$token" <id> --json resultado.json

# Salida JSON cruda para procesamiento
python3 retrieve_process.py --token "$token" <id> --format json-raw | jq .technicalCard.data.matricula
```

### 3. `list_processes.py` - Listar Documentos Procesados

Lista y filtra documentos procesados.

#### Sintaxis:

```bash
python3 list_processes.py --token <token> [opciones]
```

#### Opciones de filtrado:

- `--estado, -e`: Filtrar por estado (PENDING, COMPLETED, FAILED, etc.)
- `--tipo, -tp`: Filtrar por tipo de documento
- `--categoria, -c`: Filtrar por categoría de vehículo
- `--itv, -i`: Filtrar por estación ITV

#### Opciones de visualización:

- `--limite, -l`: Número máximo de documentos a mostrar
- `--resumen, -r`: Mostrar solo resumen estadístico
- `--json, -j ARCHIVO`: Guardar en archivo JSON
- `--orden, -o`: Ordenar por fecha (fecha-asc, fecha-desc)

#### Ejemplos:

```bash
# Listar todos los documentos
python3 list_processes.py --token "$token"

# Solo documentos completados
python3 list_processes.py --token "$token" --estado COMPLETED

# Filtrar por tipo y categoría
python3 list_processes.py --token "$token" --tipo coc --categoria M1

# Mostrar resumen estadístico
python3 list_processes.py --token "$token" --resumen

# Guardar en JSON los últimos 50 documentos
python3 list_processes.py --token "$token" --limite 50 --json documentos.json
```

### 4. `process_image_pool.py` - Procesar Imágenes

Envía imágenes para procesamiento OCR y reconocimiento.

#### Sintaxis:

```bash
python3 process_image_pool.py --token <token> --tipo <tipo> --categoria <cat> archivo1 [archivo2 ...] [opciones]
```

#### Parámetros requeridos:

- `--token, -t`: Token JWT
- `--tipo, -tp`: Tipo de documento (coc, titv-old, titv-new, etc.)
- `--categoria, -c`: Categoría del vehículo (M1, N1, etc.)

#### Opciones:

- `--nombre, -n`: Nombre descriptivo del proceso
- `--id, -i`: UUID del proceso (se genera automáticamente si no se especifica)
- `--precision, -p`: Calcular precisión (experimental)
- `-v, --verbose`: Información detallada

#### Ejemplos:

```bash
# Procesar un archivo
python3 process_image_pool.py --token "$token" --tipo coc --categoria M1 documento.pdf

# Procesar múltiples archivos
python3 process_image_pool.py --token "$token" --tipo titv-new --categoria N1 *.jpg

# Con nombre descriptivo
python3 process_image_pool.py --token "$token" --tipo coc --categoria M1 \
    --nombre "BMW X5 2024" frente.jpg reverso.jpg

# Especificar ID del proceso
python3 process_image_pool.py --token "$token" --tipo cdc --categoria L \
    --id 731cb083-7d83-4ce7-a0ce-1a3b19b7e422 certificado.pdf
```

## Ejemplos de Flujos de Trabajo Completos

### Procesamiento básico de documento

```bash
#!/bin/bash
# Guardar como: procesar_documento.sh

# Credenciales usando variables de entorno (RECOMENDADO)
export MINTITV_USER="tu_usuario"
export MINTITV_PASS="tu_contraseña"

# 1. Login
echo "Iniciando sesión..."
TOKEN=$(python3 login.py "$USUARIO" "$PASSWORD" -q)

if [ $? -ne 0 ]; then
    echo "Error en login"
    exit 1
fi

# 2. Procesar imagen
echo "Procesando documento..."
RESULTADO=$(python3 process_image_pool.py \
    --token "$TOKEN" \
    --tipo coc \
    --categoria M1 \
    --nombre "Test COC" \
    documento.pdf)

# Extraer ID del proceso
ID_PROCESO=$(echo "$RESULTADO" | grep "ID del proceso:" | awk '{print $4}')

# 3. Esperar y verificar estado
echo "Verificando estado del proceso $ID_PROCESO..."
sleep 5

python3 retrieve_process.py --token "$TOKEN" "$ID_PROCESO" --format resumen
```

### Monitoreo de procesos

```bash
#!/bin/bash
# Monitor de procesos pendientes

TOKEN="$1"  # Token como primer argumento

while true; do
    clear
    echo "=== MONITOR DE PROCESOS ==="
    echo "Fecha: $(date)"
    echo

    # Mostrar resumen
    python3 list_processes.py --token "$TOKEN" --resumen

    echo
    echo "Procesos pendientes:"
    python3 list_processes.py --token "$TOKEN" --estado PENDING --limite 5

    sleep 30
done
```

### Procesamiento en lote

```bash
#!/bin/bash
# Procesar todos los PDFs en un directorio

TOKEN="$1"
TIPO="$2"
CATEGORIA="$3"
DIRECTORIO="$4"

for archivo in "$DIRECTORIO"/*.pdf; do
    if [ -f "$archivo" ]; then
        echo "Procesando: $archivo"
        python3 process_image_pool.py \
            --token "$TOKEN" \
            --tipo "$TIPO" \
            --categoria "$CATEGORIA" \
            "$archivo"
        sleep 2  # Pausa entre procesos
    fi
done
```

## Manejo de Errores

Todos los scripts devuelven códigos de salida estándar:

- `0`: Éxito
- `1`: Error

Ejemplo de manejo de errores:

```bash
# Verificar éxito
if python3 login.py usuario clave -q > token.txt; then
    echo "Login exitoso"
    TOKEN=$(cat token.txt)
else
    echo "Error en login"
    exit 1
fi
```

## Variables de Entorno

Puedes usar variables de entorno para credenciales sensibles:

```bash
export MINTITV_USER="tu_usuario"
export MINTITV_PASS="tu_contraseña"

# En tu script:
TOKEN=$(python3 login.py "$MINTITV_USER" "$MINTITV_PASS" -q)
```

## Tipos de Documento y Categorías

### Tipos de documento (`--tipo`):

- `coc` - COC
- `titv-old` - Tarjeta ITV antigua (RD 2140/1985)
- `titv-new` - Tarjeta ITV nueva (RD 750/2010)
- `reduced` - Ficha reducida
- `single-approval` - Homologación individual
- `cdc` - Certificado de Características

### Categorías de vehículo (`--categoria`):

- `M1`, `M3` - Vehículos de pasajeros
- `N1`, `N3` - Vehículos comerciales
- `L` - Motocicletas/ciclomotores
- `O` - Remolques
- `T` - Tractores agrícolas
- `TR` - Tractores de carretera
- `OS`, `OSR` - Vehículos especiales

## Notas Importantes

- **Independencia total**: Cada script es completamente independiente
- **Sin dependencias externas**: Solo módulos nativos de Python 3
- **Tokens expiran**: Renueva el token cuando sea necesario
- **Límites de archivo**: Máximo 30MB por archivo
- **Formatos soportados**: JPG, PNG, PDF, TIFF

## Solución de Problemas

### Token expirado

```bash
# Si obtienes error 401, renueva el token:
TOKEN=$(python3 login.py "$USER" "$PASS" -q)
```

### Verificar conectividad

```bash
# Test básico de conexión con la API
# Usar la URL de la API proporcionada
```

### Debug detallado

```bash
# Usar modo verbose para más información
python3 process_image_pool.py --token "$TOKEN" --tipo coc --categoria M1 -v archivo.pdf
```

## Estructura de Archivos

```
pyscripts/
├── login.py              # Script de autenticación
├── retrieve_process.py   # Recuperar documentos
├── list_processes.py     # Listar documentos
├── process_image_pool.py # Procesar imágenes
└── README.md            # Esta documentación
```
