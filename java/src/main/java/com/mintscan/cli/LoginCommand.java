package com.mintscan.cli;

import com.mintscan.api.auth.LoginService;
import com.mintscan.api.exceptions.MintApiException;

import java.io.Console;

/**
 * Login command implementation.
 */
public class LoginCommand {
    
    public static void execute(String[] args) throws Exception {
        CommandLineParser parser = new CommandLineParser(args);
        
        if (parser.isHelpRequested()) {
            printHelp();
            return;
        }
        
        // Get credentials
        String username = null;
        String password = null;
        
        // Check command line arguments first
        if (parser.getArgumentCount() > 0) {
            username = parser.getArgument(0);
            if (parser.getArgumentCount() > 1) {
                password = parser.getArgument(1);
            }
        }
        
        // Check environment variables
        if (username == null) {
            username = System.getenv("MINTSCAN_USER");
            if (username == null) {
                System.err.println("Error: Se requiere usuario (argumento o variable MINTSCAN_USER)");
                System.exit(1);
            }
        }
        
        if (password == null) {
            password = System.getenv("MINTSCAN_PASS");
            if (password == null && !parser.hasFlag("no-interactive")) {
                // Get password interactively
                if (!parser.hasFlag("quiet") && !parser.hasFlag("q")) {
                    System.out.println("Usuario: " + username);
                }
                password = readPassword("Contraseña: ");
                if (password == null || password.isEmpty()) {
                    System.err.println("Error: Se requiere contraseña");
                    System.exit(1);
                }
            } else if (password == null) {
                System.err.println("Error: Se requiere contraseña (argumento, variable MINTSCAN_PASS o entrada interactiva)");
                System.exit(1);
            }
        }
        
        // Perform login
        LoginService loginService = new LoginService();
        
        try {
            if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                System.out.println("Iniciando sesión...");
                System.out.println("Usuario: " + username);
            }
            
            String token = loginService.login(username, password);
            
            if (parser.hasFlag("quiet") || parser.hasFlag("q")) {
                // Only print token
                System.out.println(token);
            } else {
                System.out.println("¡Inicio de sesión exitoso!");
                System.out.println("Token: " + token);
                if (parser.hasFlag("verbose") || parser.hasFlag("v")) {
                    System.out.println("\nPuedes usar este token en los otros comandos:");
                    System.out.println("  export MINTSCAN_TOKEN='" + token + "'");
                    System.out.println("  mint_scan-cli list");
                    System.out.println("  mint_scan-cli retrieve <id_proceso>");
                    System.out.println("  mint_scan-cli process --tipo coc --categoria M1 <archivo>");
                }
            }
            
        } catch (MintApiException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword(prompt);
            return passwordArray != null ? new String(passwordArray) : null;
        } else {
            // Fallback for non-console environments
            System.out.print(prompt);
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            return scanner.hasNextLine() ? scanner.nextLine() : null;
        }
    }
    
    public static void printHelp() {
        System.out.println("Uso: mint_scan-cli login [usuario] [contraseña] [opciones]");
        System.out.println();
        System.out.println("Iniciar sesión en la API de MintScan y obtener token JWT");
        System.out.println();
        System.out.println("Argumentos:");
        System.out.println("  usuario      Nombre de usuario (opcional si se usa MINTSCAN_USER)");
        System.out.println("  contraseña   Contraseña (opcional si se usa MINTSCAN_PASS o entrada interactiva)");
        System.out.println();
        System.out.println("Opciones:");
        System.out.println("  -q, --quiet           Solo mostrar el token, sin mensajes adicionales");
        System.out.println("  -v, --verbose         Mostrar información detallada");
        System.out.println("  --no-interactive      No solicitar contraseña interactivamente");
        System.out.println("  -h, --help            Mostrar esta ayuda");
        System.out.println();
        System.out.println("Variables de entorno:");
        System.out.println("  MINTSCAN_USER          Usuario para autenticación");
        System.out.println("  MINTSCAN_PASS          Contraseña del usuario");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  mint_scan-cli login miusuario");
        System.out.println("  mint_scan-cli login usuario@ejemplo.com micontraseña");
        System.out.println("  mint_scan-cli login  # Usa variables de entorno");
        System.out.println("  mint_scan-cli login -q > token.txt");
    }
}