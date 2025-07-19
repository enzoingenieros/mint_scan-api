package com.mintscan.cli;

import com.mintscan.api.exceptions.MintApiException;
import com.mintscan.api.models.*;
import com.mintscan.api.process.ProcessRetrieveService;
import com.mintscan.cli.utils.TechnicalCardPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

/**
 * Retrieve command implementation.
 */
public class RetrieveCommand {
    
    public static void execute(String[] args) throws Exception {
        CommandLineParser parser = new CommandLineParser(args);
        
        if (parser.isHelpRequested()) {
            printHelp();
            return;
        }
        
        // Get process ID
        if (parser.getArgumentCount() < 1) {
            System.err.println("Error: Se requiere el ID del proceso");
            printHelp();
            System.exit(1);
        }
        
        String processId = parser.getArgument(0);
        
        // Get token
        String token = parser.getOption("token", parser.getOption("t"));
        if (token == null) {
            token = System.getenv("MINTSCAN_TOKEN");
            if (token == null) {
                System.err.println("Error: Se requiere token (--token o variable MINTSCAN_TOKEN)");
                System.err.println("Ejecuta 'mint_scan-cli login' para obtener un token");
                System.exit(1);
            }
        }
        
        ProcessRetrieveService retrieveService = new ProcessRetrieveService();
        
        try {
            if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                System.out.println("Recuperando documento: " + processId);
            }
            
            ProcessDocument document = retrieveService.retrieveProcessedDocument(token, processId);
            
            // Display according to format
            String format = parser.getOption("format", parser.getOption("f", "completo"));
            
            switch (format) {
                case "json-raw":
                    ObjectMapper mapper = new ObjectMapper();
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(document));
                    break;
                    
                case "resumen":
                    printSummary(document);
                    break;
                    
                default: // completo
                    printFullDetails(document);
                    break;
            }
            
            // Save to JSON if requested
            String jsonFile = parser.getOption("json", parser.getOption("j"));
            if (jsonFile != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(new File(jsonFile), document);
                if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                    System.out.println("\nResultado guardado en: " + jsonFile);
                }
            }
            
        } catch (MintApiException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void printSummary(ProcessDocument document) {
        System.out.println("\nDocumento: " + document.getId());
        System.out.println("Estado: " + document.getStatus());
        
        if (document.getTechnicalCard() != null) {
            TechnicalCard tc = document.getTechnicalCard();
            System.out.println("Tipo: " + (tc.getType() != null ? tc.getType().getValue() : "N/D"));
            System.out.println("Categoría: " + (tc.getCategory() != null ? tc.getCategory() : "N/D"));
            
            if (tc.getData() != null) {
                TechnicalCardData data = tc.getData();
                System.out.println("Matrícula: " + (data.getMatricula() != null ? data.getMatricula() : "N/D"));
                System.out.println("VIN: " + (data.getE() != null ? data.getE() : "N/D"));
            }
        }
    }
    
    private static void printFullDetails(ProcessDocument document) {
        System.out.println("\n=== Detalles del Documento Procesado ===");
        System.out.println("ID: " + document.getId());
        System.out.println("Estado: " + document.getStatus());
        System.out.println("ID Documento: " + (document.getDocumentId() != null ? document.getDocumentId() : "N/D"));
        System.out.println("Creado: " + document.getCreatedAt());
        System.out.println("Actualizado: " + document.getUpdatedAt());
        
        // License information
        if (document.getLicense() != null) {
            License license = document.getLicense();
            System.out.println("\n--- Información de Licencia ---");
            System.out.println("ITV: " + license.getItv());
            System.out.println("Código: " + license.getCode());
            System.out.println("ID Cliente: " + license.getCustomerId());
        }
        
        // Technical card information
        if (document.getTechnicalCard() != null) {
            TechnicalCard tc = document.getTechnicalCard();
            System.out.println("\n--- Ficha Técnica ---");
            System.out.println("Tipo: " + (tc.getType() != null ? tc.getType().getValue() : "N/D"));
            System.out.println("Categoría: " + (tc.getCategory() != null ? tc.getCategory() : "N/D"));
            System.out.println("Modelo: " + (tc.getModel() != null ? tc.getModel() : "N/D"));
            System.out.println("Matrícula: " + (tc.getVehicleLicense() != null ? tc.getVehicleLicense() : "N/D"));
            System.out.println("VIN: " + (tc.getVin() != null ? tc.getVin() : "N/D"));
            System.out.println("ICT: " + (tc.getIct() != null ? tc.getIct() : "N/D"));
            
            // Technical card data - all fields
            if (tc.getData() != null) {
                TechnicalCardPrinter.printTechnicalCardData(tc.getData());
            }
        }
    }
    
    public static void printHelp() {
        System.out.println("Uso: mint_scan-cli retrieve <id_proceso> [opciones]");
        System.out.println();
        System.out.println("Recuperar un documento procesado específico de la API de MintScan");
        System.out.println();
        System.out.println("Argumentos:");
        System.out.println("  id_proceso            UUID del proceso a recuperar");
        System.out.println();
        System.out.println("Opciones:");
        System.out.println("  -t, --token TOKEN     Token JWT de autenticación");
        System.out.println("  -j, --json ARCHIVO    Guardar resultado completo en archivo JSON");
        System.out.println("  -f, --format FORMATO  Formato de salida (completo, resumen, json-raw)");
        System.out.println("  -v, --verbose         Mostrar información detallada");
        System.out.println("  -h, --help            Mostrar esta ayuda");
        System.out.println();
        System.out.println("Formatos de salida:");
        System.out.println("  completo              Mostrar todos los campos (default)");
        System.out.println("  resumen               Mostrar solo información básica");
        System.out.println("  json-raw              Mostrar JSON sin procesar");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  export MINTSCAN_TOKEN=\"tu-token-aqui\"");
        System.out.println("  mint_scan-cli retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422");
        System.out.println("  mint_scan-cli retrieve <id> --format resumen");
        System.out.println("  mint_scan-cli retrieve <id> --json resultado.json");
    }
}