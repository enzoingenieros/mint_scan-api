# CLI Spanish Field Descriptions Update

## Summary

Successfully updated the CLI to display Spanish field descriptions when printing technical card data.

## Changes Made

### 1. Created TechnicalCardPrinter Utility Class

**Location**: `/src/main/java/com/mintitv/cli/utils/TechnicalCardPrinter.java`

This new utility class:
- Uses Java reflection to dynamically get field values from TechnicalCardData
- Matches field codes with Spanish descriptions from TechnicalCardFieldDescriptions
- Groups fields by sections (A, B, C, D, etc.) for organized display
- Handles special fields and array fields (homologaciones, observaciones, reformas)
- Formats output with proper section headers and consistent field display

### 2. Updated RetrieveCommand

Modified RetrieveCommand to use the TechnicalCardPrinter instead of hardcoded field printing:
- Added import for TechnicalCardPrinter
- Replaced entire technical card printing section with single call to `TechnicalCardPrinter.printTechnicalCardData()`
- Removed unused `printField` method

## Example Output

Before:
```
Nº homologación CE (A.1): WVWZZZ3BZWE689725
Marca (D.1): VOLKSWAGEN
```

After:
```
====== Fabricante del Vehículo ======
Nombre del fabricante del vehículo base: WVWZZZ3BZWE689725

====== Identificación del Vehículo ======
Marca: VOLKSWAGEN
```

## Benefits

1. **Dynamic Field Display**: All fields are displayed with their exact Spanish descriptions as specified in the JSON
2. **Organized Sections**: Fields are grouped by their section codes for better readability
3. **Maintainable Code**: Adding new fields only requires updating TechnicalCardFieldDescriptions
4. **Consistent Formatting**: All fields follow the same display pattern
5. **Proper Handling of Special Characters**: Supports Greek letter Κ (Kappa) in field codes

## Build Status

✅ The project builds successfully with all changes.

## Testing

To test the new output format:
```bash
java -jar target/mintitv-cli.jar retrieve <process-id> --format completo
```

The technical card data will now display with proper Spanish field descriptions matching the provided JSON values.