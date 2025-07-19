import { Command } from 'commander';
import { writeFileSync } from 'node:fs';
import { ListService } from '../api/services/listService.ts';
import { ENV_VARS, PROCESS_STATUSES, DOCUMENT_TYPES, VEHICLE_CATEGORIES } from '../models/constants.ts';
import { handleApiError } from '../core/errorHandler.ts';
import type { ProcessDocumentItem, ProcessStatus, DocumentType, VehicleCategory } from '../models/types.ts';

function printSummary(documents: ProcessDocumentItem[], listService: ListService): void {
  const stats = listService.getStatistics(documents);
  
  console.log('\n=== Resumen de Documentos Procesados ===');
  console.log(`Total de documentos: ${stats.total}`);
  
  console.log('\nPor Estado:');
  for (const [status, count] of Object.entries(stats.statusCounts)) {
    console.log(`  ${status}: ${count}`);
  }
  
  console.log('\nPor Tipo de Documento:');
  for (const [type, count] of Object.entries(stats.typeCounts)) {
    console.log(`  ${type}: ${count}`);
  }
  
  console.log('\nPor Estación ITV:');
  for (const [itv, count] of Object.entries(stats.itvCounts)) {
    console.log(`  ${itv}: ${count}`);
  }
}

function printDocumentList(documents: ProcessDocumentItem[], limit: number): void {
  const count = Math.min(documents.length, limit);
  console.log(`\n=== Documentos Procesados (mostrando ${count} de ${documents.length}) ===`);
  
  for (let i = 0; i < count; i++) {
    const doc = documents[i]!;
    console.log(`\n${i + 1}. ID: ${doc.id}`);
    console.log(`   Estado: ${doc.status}`);
    if (doc.technicalCard) {
      console.log(`   Tipo: ${doc.technicalCard.type || 'N/D'}`);
      console.log(`   Categoría: ${doc.technicalCard.category || 'N/D'}`);
      console.log(`   Modelo: ${doc.technicalCard.model || 'N/D'}`);
    }
    if (doc.license) {
      console.log(`   ITV: ${doc.license.itv}`);
    }
    console.log(`   Creado: ${doc.createdAt}`);
  }
  
  if (documents.length > limit) {
    console.log(`\n(Mostrando ${limit} de ${documents.length} documentos)`);
  }
}

export function createListCommand(): Command {
  const command = new Command('list');
  
  command
    .description('Listar documentos procesados de la API de MintScan')
    .option('-t, --token <TOKEN>', 'Token JWT de autenticación')
    .option('-e, --estado <ESTADO>', `Filtrar por estado (${PROCESS_STATUSES.join(', ')})`)
    .option('--tipo <TIPO>', `Filtrar por tipo de documento (${DOCUMENT_TYPES.join(', ')})`)
    .option('-c, --categoria <CAT>', `Filtrar por categoría (${VEHICLE_CATEGORIES.join(', ')})`)
    .option('-i, --itv <ITV>', 'Filtrar por estación ITV')
    .option('-l, --limite <N>', 'Número máximo de documentos a mostrar', '10')
    .option('-r, --resumen', 'Mostrar solo resumen estadístico')
    .option('-j, --json <ARCHIVO>', 'Guardar resultado completo en archivo JSON')
    .option('-o, --orden <ORDEN>', 'Ordenar por fecha (fecha-asc, fecha-desc)', 'fecha-desc')
    .option('-v, --verbose', 'Mostrar información detallada')
    .action(async (options) => {
      try {
        // Get token
        const token = options.token || process.env[ENV_VARS.TOKEN];
        if (!token) {
          console.error('Error: Se requiere token (--token o variable MINTSCAN_TOKEN)');
          console.error("Ejecuta 'mint_scan-cli login' para obtener un token");
          process.exit(1);
        }

        const listService = new ListService();

        if (options.verbose) {
          console.log('Obteniendo documentos procesados...');
        }

        let documents = await listService.listProcessedDocuments(token);

        if (options.verbose) {
          console.log(`Total de documentos obtenidos: ${documents.length}`);
        }

        // Apply filters
        if (options.estado) {
          const status = options.estado.toUpperCase() as ProcessStatus;
          if (!PROCESS_STATUSES.includes(status)) {
            throw new Error(`Estado inválido: ${options.estado}. Valores válidos: ${PROCESS_STATUSES.join(', ')}`);
          }
          documents = listService.filterByStatus(documents, status);
          if (options.verbose) {
            console.log(`Filtrado por estado '${options.estado}': ${documents.length} documentos`);
          }
        }

        if (options.tipo) {
          const type = options.tipo as DocumentType;
          if (!DOCUMENT_TYPES.includes(type)) {
            throw new Error(`Tipo inválido: ${options.tipo}. Valores válidos: ${DOCUMENT_TYPES.join(', ')}`);
          }
          documents = listService.filterByType(documents, type);
          if (options.verbose) {
            console.log(`Filtrado por tipo '${options.tipo}': ${documents.length} documentos`);
          }
        }

        if (options.categoria) {
          const category = options.categoria.toUpperCase() as VehicleCategory;
          if (!VEHICLE_CATEGORIES.includes(category)) {
            throw new Error(`Categoría inválida: ${options.categoria}. Valores válidas: ${VEHICLE_CATEGORIES.join(', ')}`);
          }
          documents = listService.filterByCategory(documents, category);
          if (options.verbose) {
            console.log(`Filtrado por categoría '${options.categoria}': ${documents.length} documentos`);
          }
        }

        if (options.itv) {
          documents = listService.filterByItv(documents, options.itv);
          if (options.verbose) {
            console.log(`Filtrado por ITV '${options.itv}': ${documents.length} documentos`);
          }
        }

        // Sort
        const descending = options.orden === 'fecha-desc';
        documents = listService.sortByDate(documents, true, descending);

        // Display results
        if (options.resumen) {
          printSummary(documents, listService);
        } else {
          if (documents.length === 0) {
            console.log('No se encontraron documentos con los filtros especificados.');
          } else {
            const limit = parseInt(options.limite, 10);
            printDocumentList(documents, limit);
          }
        }

        // Save to JSON if requested
        if (options.json) {
          writeFileSync(options.json, JSON.stringify(documents, null, 2), 'utf-8');
          if (options.verbose) {
            console.log(`\nResultados guardados en: ${options.json}`);
          }
        }
      } catch (error) {
        handleApiError(error);
      }
    });

  return command;
}