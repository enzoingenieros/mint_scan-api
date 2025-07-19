#!/bin/bash

# Ejemplos de uso del CLI MintScan con Bun

echo "=== MintScan CLI - Ejemplos de uso ==="
echo

# 1. Login y guardar token
echo "1. Iniciar sesión y guardar token:"
echo "   bun run src/index.ts login usuario@ejemplo.com contraseña -q > token.txt"
echo "   export MINTSCAN_TOKEN=\$(cat token.txt)"
echo

# 2. Listar documentos
echo "2. Listar documentos procesados:"
echo "   # Todos los documentos"
echo "   bun run src/index.ts list"
echo
echo "   # Solo completados"
echo "   bun run src/index.ts list --estado COMPLETED"
echo
echo "   # Filtrar por tipo y categoría"
echo "   bun run src/index.ts list --tipo coc --categoria M1"
echo

# 3. Procesar documentos
echo "3. Procesar documentos:"
echo "   # Un solo archivo"
echo "   bun run src/index.ts process --tipo coc --categoria M1 documento.pdf"
echo
echo "   # Múltiples archivos"
echo "   bun run src/index.ts process --tipo titv-new --categoria N1 front.jpg back.jpg"
echo

# 4. Recuperar documento
echo "4. Recuperar documento específico:"
echo "   # Detalles completos"
echo "   bun run src/index.ts retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422"
echo
echo "   # Solo resumen"
echo "   bun run src/index.ts retrieve <id> --format resumen"
echo

# 5. Flujo completo
echo "5. Flujo completo de trabajo:"
echo "   # Login"
echo "   export MINTSCAN_TOKEN=\$(bun run src/index.ts login usuario pass -q)"
echo
echo "   # Procesar"
echo "   ID=\$(bun run src/index.ts process --tipo coc --categoria M1 doc.pdf | grep 'ID del proceso:' | cut -d' ' -f4)"
echo
echo "   # Esperar y recuperar"
echo "   sleep 30"
echo "   bun run src/index.ts retrieve \$ID"
echo

echo "=== Compilar a binario (opcional) ==="
echo "bun build src/index.ts --compile --outfile mint_scan-cli"
echo "./mint_scan-cli --help"