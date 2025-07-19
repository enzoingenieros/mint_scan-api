package com.mintitv.cli;

import com.mintitv.api.exceptions.MintApiException;
import com.mintitv.api.models.*;
import com.mintitv.api.process.ProcessListService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * List command implementation.
 */
public class ListCommand {
    
    public static void execute(String[] args) throws Exception {
        CommandLineParser parser = new CommandLineParser(args);
        
        if (parser.isHelpRequested()) {
            printHelp();
            return;
        }
        
        // Get token
        String token = parser.getOption("token", parser.getOption("t"));
        if (token == null) {
            token = System.getenv("MINTITV_TOKEN");
            if (token == null) {
                System.err.println("Error: Se requiere token (--token o variable MINTITV_TOKEN)");
                System.err.println("Ejecuta 'mintitv-cli login' para obtener un token");
                System.exit(1);
            }
        }
        
        ProcessListService listService = new ProcessListService();
        
        try {
            if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                System.out.println("Obteniendo documentos procesados...");
            }
            
            List<ProcessDocument> documents = listService.listProcessedDocuments(token);
            
            if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                System.out.println("Total de documentos obtenidos: " + documents.size());
            }
            
            // Apply filters
            String statusFilter = parser.getOption("estado", parser.getOption("e"));
            if (statusFilter != null) {
                ProcessStatus status = ProcessStatus.valueOf(statusFilter.toUpperCase());
                documents = listService.filterByStatus(documents, status);
                if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                    System.out.println("Filtrado por estado '" + statusFilter + "': " + documents.size() + " documentos");
                }
            }
            
            String typeFilter = parser.getOption("tipo", parser.getOption("tp"));
            if (typeFilter != null) {
                DocumentType type = DocumentType.fromValue(typeFilter);
                documents = listService.filterByType(documents, type);
                if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                    System.out.println("Filtrado por tipo '" + typeFilter + "': " + documents.size() + " documentos");
                }
            }
            
            String categoryFilter = parser.getOption("categoria", parser.getOption("c"));
            if (categoryFilter != null) {
                VehicleCategory category = VehicleCategory.valueOf(categoryFilter.toUpperCase());
                documents = listService.filterByCategory(documents, category);
                if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                    System.out.println("Filtrado por categoría '" + categoryFilter + "': " + documents.size() + " documentos");
                }
            }
            
            String itvFilter = parser.getOption("itv", parser.getOption("i"));
            if (itvFilter != null) {
                documents = listService.filterByItv(documents, itvFilter);
                if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                    System.out.println("Filtrado por ITV '" + itvFilter + "': " + documents.size() + " documentos");
                }
            }
            
            // Sort
            String order = parser.getOption("orden", parser.getOption("o", "fecha-desc"));
            boolean descending = order.equals("fecha-desc");
            documents = listService.sortByDate(documents, true, descending);
            
            // Display results
            if (parser.hasFlag("resumen") || parser.hasFlag("r")) {
                printSummary(documents, listService);
            } else {
                if (documents.isEmpty()) {
                    System.out.println("No se encontraron documentos con los filtros especificados.");
                } else {
                    int limit = Integer.parseInt(parser.getOption("limite", parser.getOption("l", "10")));
                    printDocumentList(documents, limit);
                }
            }
            
            // Save to JSON if requested
            String jsonFile = parser.getOption("json", parser.getOption("j"));
            if (jsonFile != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(new File(jsonFile), documents);
                if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                    System.out.println("\nResultados guardados en: " + jsonFile);
                }
            }
            
        } catch (MintApiException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void printSummary(List<ProcessDocument> documents, ProcessListService service) {
        ProcessListService.DocumentStatistics stats = service.getStatistics(documents);
        
        System.out.println("\n=== Resumen de Documentos Procesados ===");
        System.out.println("Total de documentos: " + stats.getTotal());
        
        System.out.println("\nPor Estado:");
        for (Map.Entry<ProcessStatus, Long> entry : stats.getStatusCounts().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\nPor Tipo de Documento:");
        for (Map.Entry<DocumentType, Long> entry : stats.getTypeCounts().entrySet()) {
            System.out.println("  " + entry.getKey().getValue() + ": " + entry.getValue());
        }
        
        System.out.println("\nPor Estación ITV:");
        for (Map.Entry<String, Long> entry : stats.getItvCounts().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
    
    private static void printDocumentList(List<ProcessDocument> documents, int limit) {
        int count = Math.min(documents.size(), limit);
        System.out.println("\n=== Documentos Procesados (mostrando " + count + " de " + documents.size() + ") ===");
        
        for (int i = 0; i < count; i++) {
            ProcessDocument doc = documents.get(i);
            System.out.println("\n" + (i + 1) + ". ID: " + doc.getId());
            System.out.println("   Estado: " + doc.getStatus());
            if (doc.getTechnicalCard() != null) {
                TechnicalCard tc = doc.getTechnicalCard();
                System.out.println("   Tipo: " + (tc.getType() != null ? tc.getType().getValue() : "N/D"));
                System.out.println("   Categoría: " + (tc.getCategory() != null ? tc.getCategory() : "N/D"));
                System.out.println("   Modelo: " + (tc.getModel() != null ? tc.getModel() : "N/D"));
            }
            if (doc.getLicense() != null) {
                System.out.println("   ITV: " + doc.getLicense().getItv());
            }
            System.out.println("   Creado: " + doc.getCreatedAt());
        }
        
        if (documents.size() > limit) {
            System.out.println("\n(Mostrando " + limit + " de " + documents.size() + " documentos)");
        }
    }
    
    public static void printHelp() {
        System.out.println("Uso: mintitv-cli list [opciones]");
        System.out.println();
        System.out.println("Listar documentos procesados de la API de MintITV");
        System.out.println();
        System.out.println("Opciones:");
        System.out.println("  -t, --token TOKEN     Token JWT de autenticación");
        System.out.println("  -e, --estado ESTADO   Filtrar por estado (PENDING, COMPLETED, etc.)");
        System.out.println("  --tipo TIPO           Filtrar por tipo de documento (coc, titv-new, etc.)");
        System.out.println("  -c, --categoria CAT   Filtrar por categoría (M1, N1, etc.)");
        System.out.println("  -i, --itv ITV         Filtrar por estación ITV");
        System.out.println("  -l, --limite N        Número máximo de documentos a mostrar (default: 10)");
        System.out.println("  -r, --resumen         Mostrar solo resumen estadístico");
        System.out.println("  -j, --json ARCHIVO    Guardar resultado completo en archivo JSON");
        System.out.println("  -o, --orden ORDEN     Ordenar por fecha (fecha-asc, fecha-desc)");
        System.out.println("  -v, --verbose         Mostrar información detallada");
        System.out.println("  -h, --help            Mostrar esta ayuda");
        System.out.println();
        System.out.println("Estados válidos:");
        System.out.println("  PENDING, STRAIGHTENING, RECOGNIZING, COMPLETED, FAILED, RETRIEVED, ABORTED");
        System.out.println();
        System.out.println("Tipos de documento:");
        System.out.println("  coc, titv-old, titv-new, reduced, single-approval, cdc");
        System.out.println();
        System.out.println("Categorías de vehículo:");
        System.out.println("  M1, M3, N1, N3, L, O, T, TR, OS, OSR");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  export MINTITV_TOKEN=\"tu-token-aqui\"");
        System.out.println("  mintitv-cli list");
        System.out.println("  mintitv-cli list --estado COMPLETED");
        System.out.println("  mintitv-cli list --tipo coc --categoria M1");
        System.out.println("  mintitv-cli list --resumen");
        System.out.println("  mintitv-cli list --json documentos.json");
    }
}