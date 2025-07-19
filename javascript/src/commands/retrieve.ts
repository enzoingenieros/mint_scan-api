import { Command } from 'commander';
import { writeFileSync } from 'node:fs';
import { ProcessService } from '../api/services/processService.ts';
import { ENV_VARS } from '../models/constants.ts';
import { handleApiError } from '../core/errorHandler.ts';
import { printTechnicalCardData } from '../utils/printer.ts';
import type { ProcessedDocument } from '../models/types.ts';

function printSummary(document: ProcessedDocument): void {
  console.log(`\nDocumento: ${document.id}`);
  console.log(`Estado: ${document.status}`);
  
  if (document.technicalCard) {
    const tc = document.technicalCard;
    console.log(`Tipo: ${tc.type || 'N/D'}`);
    console.log(`Categoría: ${tc.category || 'N/D'}`);
    
    if (tc.data) {
      console.log(`Matrícula: ${tc.data.matricula || 'N/D'}`);
      console.log(`VIN: ${tc.data.E || 'N/D'}`);
    }
  }
}

function printFullDetails(document: ProcessedDocument): void {
  console.log('\n=== Detalles del Documento Procesado ===');
  console.log(`ID: ${document.id}`);
  console.log(`Estado: ${document.status}`);
  console.log(`ID Documento: ${document.documentId || 'N/D'}`);
  console.log(`Creado: ${document.createdAt}`);
  console.log(`Actualizado: ${document.updatedAt}`);
  
  // License information
  if (document.license) {
    const license = document.license;
    console.log('\n--- Información de Licencia ---');
    console.log(`ITV: ${license.itv}`);
    console.log(`Código: ${license.code}`);
    console.log(`ID Cliente: ${license.customerId}`);
  }
  
  // Technical card information
  if (document.technicalCard) {
    const tc = document.technicalCard;
    console.log('\n--- Ficha Técnica ---');
    console.log(`Tipo: ${tc.type || 'N/D'}`);
    console.log(`Categoría: ${tc.category || 'N/D'}`);
    console.log(`Modelo: ${tc.model || 'N/D'}`);
    console.log(`Matrícula: ${tc.vehicleLicense || 'N/D'}`);
    console.log(`VIN: ${tc.vin || 'N/D'}`);
    console.log(`ICT: ${tc.ict !== null ? tc.ict : 'N/D'}`);
    
    // Technical card data - all fields
    if (tc.data) {
      printTechnicalCardData(tc.data);
    }
  }
}

export function createRetrieveCommand(): Command {
  const command = new Command('retrieve');
  
  command
    .description('Recuperar un documento procesado específico de la API de MintScan')
    .argument('<id_proceso>', 'UUID del proceso a recuperar')
    .option('-t, --token <TOKEN>', 'Token JWT de autenticación')
    .option('-j, --json <ARCHIVO>', 'Guardar resultado completo en archivo JSON')
    .option('-f, --format <FORMATO>', 'Formato de salida (completo, resumen, json-raw)', 'completo')
    .option('-v, --verbose', 'Mostrar información detallada')
    .action(async (idProceso: string, options) => {
      try {
        // Get token
        const token = options.token || process.env[ENV_VARS.TOKEN];
        if (!token) {
          console.error('Error: Se requiere token (--token o variable MINTSCAN_TOKEN)');
          console.error("Ejecuta 'mint_scan-cli login' para obtener un token");
          process.exit(1);
        }

        const processService = new ProcessService();

        if (options.verbose) {
          console.log(`Recuperando documento: ${idProceso}`);
        }

        const document = await processService.retrieveProcessedDocument(token, idProceso);

        // Display according to format
        switch (options.format) {
          case 'json-raw':
            console.log(JSON.stringify(document, null, 2));
            break;
            
          case 'resumen':
            printSummary(document);
            break;
            
          default: // completo
            printFullDetails(document);
            break;
        }

        // Save to JSON if requested
        if (options.json) {
          writeFileSync(options.json, JSON.stringify(document, null, 2), 'utf-8');
          if (options.verbose) {
            console.log(`\nResultado guardado en: ${options.json}`);
          }
        }
      } catch (error) {
        handleApiError(error);
      }
    });

  return command;
}