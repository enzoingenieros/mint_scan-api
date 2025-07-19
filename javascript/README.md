# MintITV CLI - TypeScript/Bun

CLI para interactuar con la API de MintITV, implementado en TypeScript usando Bun como runtime.

## Características

- 🚀 Ejecutado con Bun para máximo rendimiento
- 📝 Escrito en TypeScript con tipos estrictos
- 🔐 Autenticación JWT con soporte para variables de entorno
- 📄 Procesamiento de documentos (PDF, imágenes)
- 📊 Listado y filtrado de documentos procesados
- 🔍 Recuperación de documentos específicos
- 💾 Exportación de resultados a JSON

## Requisitos

- [Bun](https://bun.sh/) v1.0 o superior

## Instalación

```bash
# Clonar el repositorio
git clone <repository-url>
cd javascript

# Instalar dependencias
bun install

# Hacer el CLI ejecutable (opcional)
chmod +x src/index.ts
```

## Uso

### Ejecutar directamente con Bun

```bash
bun run src/index.ts [comando] [opciones]
```

### Compilar a binario (opcional)

```bash
bun build src/index.ts --compile --outfile mintitv-cli
./mintitv-cli [comando] [opciones]
```

## Comandos

### Login

Autenticarse en la API y obtener token JWT:

```bash
# Con usuario y contraseña como argumentos
bun run src/index.ts login usuario@ejemplo.com contraseña

# Con usuario como argumento (solicita contraseña)
bun run src/index.ts login usuario@ejemplo.com

# Usando variables de entorno
export MINTITV_USER="usuario@ejemplo.com"
export MINTITV_PASS="contraseña"
bun run src/index.ts login

# Solo mostrar el token
bun run src/index.ts login -q > token.txt
```

### List

Listar documentos procesados:

```bash
# Listar todos los documentos
bun run src/index.ts list

# Filtrar por estado
bun run src/index.ts list --estado COMPLETED

# Filtrar por tipo y categoría
bun run src/index.ts list --tipo coc --categoria M1

# Mostrar resumen estadístico
bun run src/index.ts list --resumen

# Guardar resultados en JSON
bun run src/index.ts list --json documentos.json

# Limitar número de resultados
bun run src/index.ts list --limite 20
```

Opciones de filtrado:
- `--estado`: PENDING, STRAIGHTENING, RECOGNIZING, COMPLETED, FAILED, RETRIEVED, ABORTED
- `--tipo`: coc, titv-old, titv-new, reduced, single-approval, cdc
- `--categoria`: M1, M3, N1, N3, L, O, T, TR, OS, OSR
- `--itv`: Código de estación ITV

### Process

Procesar imágenes de documentos:

```bash
# Procesar un archivo
bun run src/index.ts process --tipo coc --categoria M1 documento.pdf

# Procesar múltiples archivos
bun run src/index.ts process --tipo titv-new --categoria N1 frontal.jpg trasera.jpg

# Con nombre descriptivo
bun run src/index.ts process --tipo coc --categoria M1 --nombre "BMW 2024" doc.pdf

# Especificar ID del proceso
bun run src/index.ts process --tipo coc --categoria M1 --id 731cb083-7d83-4ce7-a0ce-1a3b19b7e422 imagen.jpg
```

Formatos soportados:
- JPEG (.jpg, .jpeg)
- PNG (.png)
- PDF (.pdf)
- TIFF (.tif, .tiff)

### Retrieve

Recuperar un documento procesado específico:

```bash
# Mostrar detalles completos
bun run src/index.ts retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422

# Mostrar solo resumen
bun run src/index.ts retrieve <id> --format resumen

# Mostrar JSON sin formato
bun run src/index.ts retrieve <id> --format json-raw

# Guardar en archivo
bun run src/index.ts retrieve <id> --json resultado.json
```

## Variables de Entorno

- `MINTITV_USER`: Usuario para autenticación
- `MINTITV_PASS`: Contraseña del usuario
- `MINTITV_TOKEN`: Token JWT (evita tener que hacer login)

### Ejemplo de uso con token guardado

```bash
# Guardar token después del login
export MINTITV_TOKEN=$(bun run src/index.ts login usuario contraseña -q)

# Usar el token en comandos posteriores
bun run src/index.ts list
bun run src/index.ts process --tipo coc --categoria M1 documento.pdf
bun run src/index.ts retrieve <id>
```

## Desarrollo

### Estructura del proyecto

```
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
└── README.md
```

### Ejecutar en modo desarrollo

```bash
bun --watch src/index.ts [comando]
```

### Verificar tipos

```bash
bun run typecheck
```

### Ejecutar tests

```bash
bun test
```

## Seguridad

- Las contraseñas nunca se muestran en pantalla
- Se recomienda usar variables de entorno para credenciales
- Los tokens JWT deben manejarse de forma segura
- Tamaño máximo de archivo: 30MB

## Comparación con otras implementaciones

Esta implementación en TypeScript/Bun ofrece:
- ✅ Mejor rendimiento que Node.js
- ✅ Tipos estáticos para mayor seguridad
- ✅ Sintaxis moderna de ES2022+
- ✅ Compatibilidad con las implementaciones Java y Python
- ✅ Mismo conjunto de comandos y opciones

## Licencia

MIT