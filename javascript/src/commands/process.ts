import { Command } from 'commander';
import { ProcessService } from '../api/services/processService.ts';
import { ENV_VARS, DOCUMENT_TYPES, VEHICLE_CATEGORIES } from '../models/constants.ts';
import { handleApiError } from '../core/errorHandler.ts';
import { 
  validateFiles, 
  validateDocumentType, 
  validateVehicleCategory, 
  validateUUID,
  hasValidFiles,
  hasErrors
} from '../utils/validation.ts';

export function createProcessCommand(): Command {
  const command = new Command('process');
  
  command
    .description('Procesar imágenes de documentos')
    .argument('<archivos...>', 'Archivos de imagen o PDF a procesar')
    .requiredOption('--tipo <TIPO>', `Tipo de documento (${DOCUMENT_TYPES.join(', ')})`)
    .requiredOption('-c, --categoria <CAT>', `Categoría del vehículo (${VEHICLE_CATEGORIES.join(', ')})`)
    .option('-t, --token <TOKEN>', 'Token JWT de autenticación')
    .option('-n, --nombre <NOMBRE>', 'Nombre descriptivo para el proceso (máx 100 caracteres)')
    .option('-i, --id <UUID>', 'UUID del proceso (se genera automáticamente si no se especifica)')
    .option('-p, --precision', 'Calcular precisión de extracción (experimental)')
    .option('-v, --verbose', 'Mostrar información detallada')
    .action(async (archivos: string[], options) => {
      try {
        // Get token
        const token = options.token || process.env[ENV_VARS.TOKEN];
        if (!token) {
          console.error('Error: Se requiere token (--token o variable MINTSCAN_TOKEN)');
          console.error("Ejecuta 'mint_scan-cli login' para obtener un token");
          process.exit(1);
        }

        // Validate document type and category
        const documentType = validateDocumentType(options.tipo);
        const vehicleCategory = validateVehicleCategory(options.categoria);

        // Validate process ID if provided
        let processId: string | undefined;
        if (options.id) {
          processId = validateUUID(options.id);
        }

        // Validate files
        const validationResult = validateFiles(archivos);
        
        if (!hasValidFiles(validationResult)) {
          throw new Error('No hay archivos válidos para procesar');
        }

        if (hasErrors(validationResult)) {
          console.error('Advertencias: ' + validationResult.errors.join(', '));
        }

        const processService = new ProcessService();

        if (options.verbose) {
          console.log(`Procesando ${validationResult.validFiles.length} archivo(s)...`);
          console.log(`ID del proceso: ${processId || 'Se generará automáticamente'}`);
          console.log(`Tipo de documento: ${documentType}`);
          console.log(`Categoría del vehículo: ${vehicleCategory}`);
          if (options.nombre) {
            console.log(`Nombre: ${options.nombre}`);
          }
        }

        // Process files
        let response;
        if (validationResult.validFiles.length === 1) {
          response = await processService.processSingleImage(
            token,
            validationResult.validFiles[0]!,
            documentType,
            vehicleCategory,
            options.nombre,
            options.precision,
            processId
          );
        } else {
          response = await processService.processMultipleImages(
            token,
            validationResult.validFiles,
            documentType,
            vehicleCategory,
            options.nombre,
            options.precision,
            processId
          );
        }

        // Display results
        if (response.success) {
          console.log('\n✓ Procesamiento iniciado exitosamente');
          console.log(`ID del proceso: ${response.id}`);
          console.log('\nPuedes verificar el estado con:');
          console.log(`  mint_scan-cli retrieve ${response.id}`);
        } else {
          console.log('\n✗ Error en el procesamiento');
          if (options.verbose) {
            console.log(JSON.stringify(response, null, 2));
          }
        }
      } catch (error) {
        handleApiError(error);
      }
    });

  return command;
}