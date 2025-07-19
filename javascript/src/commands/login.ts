import { Command } from 'commander';
import { AuthService } from '../api/auth.ts';
import { ENV_VARS } from '../models/constants.ts';
import { handleApiError } from '../core/errorHandler.ts';

async function readPasswordFromStdin(promptText: string): Promise<string> {
  process.stdout.write(promptText);
  
  return new Promise((resolve) => {
    process.stdin.setRawMode(true);
    process.stdin.resume();
    process.stdin.setEncoding('utf8');
    
    let password = '';
    
    process.stdin.on('data', (data) => {
      const char = data.toString();
      
      switch (char) {
        case '\n':
        case '\r':
        case '\u0004':
          process.stdin.setRawMode(false);
          process.stdin.pause();
          process.stdin.removeAllListeners('data');
          console.log();
          resolve(password);
          break;
        case '\u0003':
          process.exit();
        case '\u007f':
          password = password.slice(0, -1);
          break;
        default:
          password += char;
          break;
      }
    });
  });
}

async function getPasswordInteractive(): Promise<string> {
  // Try to use stdin for password input to hide characters
  try {
    return await readPasswordFromStdin('Contraseña: ');
  } catch (error) {
    // Fallback to regular prompt if stdin fails
    const password = global.prompt('Contraseña: ');
    if (!password) {
      throw new Error('Se requiere contraseña');
    }
    return password;
  }
}

export function createLoginCommand(): Command {
  const command = new Command('login');
  
  command
    .description('Iniciar sesión en la API de MintITV y obtener token JWT')
    .argument('[usuario]', 'Nombre de usuario (opcional si se usa MINTITV_USER)')
    .argument('[contraseña]', 'Contraseña (opcional si se usa MINTITV_PASS o entrada interactiva)')
    .option('-q, --quiet', 'Solo mostrar el token, sin mensajes adicionales')
    .option('-v, --verbose', 'Mostrar información detallada')
    .option('--no-interactive', 'No solicitar contraseña interactivamente')
    .action(async (usuarioArg, contraseñaArg, options) => {
      try {
        // Get username
        let username = usuarioArg;
        if (!username) {
          username = process.env[ENV_VARS.USER];
          if (!username) {
            console.error('Error: Se requiere usuario (argumento o variable MINTITV_USER)');
            process.exit(1);
          }
        }

        // Get password
        let password = contraseñaArg;
        if (!password) {
          password = process.env[ENV_VARS.PASS];
          if (!password && !options.noInteractive) {
            if (!options.quiet) {
              console.log(`Usuario: ${username}`);
            }
            password = await getPasswordInteractive();
          } else if (!password) {
            console.error('Error: Se requiere contraseña (argumento, variable MINTITV_PASS o entrada interactiva)');
            process.exit(1);
          }
        }

        // Perform login
        const authService = new AuthService();
        
        if (options.verbose) {
          console.log('Iniciando sesión...');
          console.log(`Usuario: ${username}`);
        }

        const token = await authService.login(username, password);

        if (options.quiet) {
          // Only print token
          console.log(token);
        } else {
          console.log('¡Inicio de sesión exitoso!');
          console.log(`Token: ${token}`);
          if (options.verbose) {
            console.log('\nPuedes usar este token en los otros comandos:');
            console.log(`  export MINTITV_TOKEN='${token}'`);
            console.log('  mintitv-cli list');
            console.log('  mintitv-cli retrieve <id_proceso>');
            console.log('  mintitv-cli process --tipo coc --categoria M1 <archivo>');
          }
        }
      } catch (error) {
        handleApiError(error);
      }
    });

  return command;
}