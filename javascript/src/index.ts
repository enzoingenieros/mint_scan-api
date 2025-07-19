#!/usr/bin/env bun
import { Command } from 'commander';
import { createLoginCommand } from './commands/login.ts';
import { createListCommand } from './commands/list.ts';
import { createProcessCommand } from './commands/process.ts';
import { createRetrieveCommand } from './commands/retrieve.ts';

const VERSION = '1.0.0';
const APP_NAME = 'mintitv-cli';

const program = new Command();

program
  .name(APP_NAME)
  .description('CLI para operaciones con la API de MintITV')
  .version(VERSION)
  .addHelpText('after', `
Variables de entorno:
  MINTITV_USER    Usuario para autenticación
  MINTITV_PASS    Contraseña del usuario
  MINTITV_TOKEN   Token JWT para evitar login

Ejemplos:
  ${APP_NAME} login miusuario
  ${APP_NAME} list --estado COMPLETED
  ${APP_NAME} process --tipo coc --categoria M1 documento.pdf
  ${APP_NAME} retrieve 731cb083-7d83-4ce7-a0ce-1a3b19b7e422
`);

// Add commands
program.addCommand(createLoginCommand());
program.addCommand(createListCommand());
program.addCommand(createProcessCommand());
program.addCommand(createRetrieveCommand());

// Parse command line arguments
program.parse();

// Show help if no command provided
if (!process.argv.slice(2).length) {
  program.outputHelp();
  process.exit(1);
}