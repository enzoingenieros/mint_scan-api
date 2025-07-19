package com.mintitv.cli;

import java.util.Arrays;

/**
 * Main CLI application for MintITV API operations.
 */
public class MintItvCli {
    
    private static final String VERSION = "1.0.0";
    private static final String APP_NAME = "mintitv-cli";
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }
        
        String command = args[0].toLowerCase();
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
        
        try {
            switch (command) {
                case "login":
                    LoginCommand.execute(commandArgs);
                    break;
                    
                case "list":
                    ListCommand.execute(commandArgs);
                    break;
                    
                case "retrieve":
                    RetrieveCommand.execute(commandArgs);
                    break;
                    
                case "process":
                    ProcessCommand.execute(commandArgs);
                    break;
                    
                case "help":
                case "--help":
                case "-h":
                    if (commandArgs.length > 0) {
                        printCommandHelp(commandArgs[0]);
                    } else {
                        printUsage();
                    }
                    break;
                    
                case "version":
                case "--version":
                case "-v":
                    System.out.println(APP_NAME + " version " + VERSION);
                    break;
                    
                default:
                    System.err.println("Error: Comando desconocido '" + command + "'");
                    System.err.println();
                    printUsage();
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void printUsage() {
        System.out.println("Uso: " + APP_NAME + " <comando> [opciones]");
        System.out.println();
        System.out.println("Comandos disponibles:");
        System.out.println("  login      Iniciar sesión y obtener token JWT");
        System.out.println("  list       Listar documentos procesados");
        System.out.println("  retrieve   Recuperar un documento específico");
        System.out.println("  process    Procesar imágenes de documentos");
        System.out.println("  help       Mostrar ayuda general o de un comando específico");
        System.out.println("  version    Mostrar versión del programa");
        System.out.println();
        System.out.println("Para ayuda sobre un comando específico:");
        System.out.println("  " + APP_NAME + " help <comando>");
        System.out.println("  " + APP_NAME + " <comando> --help");
        System.out.println();
        System.out.println("Variables de entorno:");
        System.out.println("  MINTITV_USER    Usuario para autenticación");
        System.out.println("  MINTITV_PASS    Contraseña del usuario");
        System.out.println("  MINTITV_TOKEN   Token JWT para evitar login");
    }
    
    private static void printCommandHelp(String command) {
        switch (command.toLowerCase()) {
            case "login":
                LoginCommand.printHelp();
                break;
            case "list":
                ListCommand.printHelp();
                break;
            case "retrieve":
                RetrieveCommand.printHelp();
                break;
            case "process":
                ProcessCommand.printHelp();
                break;
            default:
                System.err.println("Error: Comando desconocido '" + command + "'");
                printUsage();
        }
    }
}