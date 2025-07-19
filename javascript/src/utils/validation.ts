import { z } from 'zod';
import { 
  DOCUMENT_TYPES, 
  VEHICLE_CATEGORIES, 
  SUPPORTED_EXTENSIONS,
  MAX_FILE_SIZE,
} from '../models/constants.ts';
import { statSync, existsSync } from 'node:fs';
import { extname } from 'node:path';

export const UUIDSchema = z.string().uuid();

export const DocumentTypeSchema = z.enum(DOCUMENT_TYPES);

export const VehicleCategorySchema = z.enum(VEHICLE_CATEGORIES);

export const UsernameSchema = z.string().min(1).max(100);

export const PasswordSchema = z.string().min(8).max(100);

export function validateUUID(value: string): string {
  try {
    return UUIDSchema.parse(value);
  } catch {
    throw new Error(`ID inválido (debe ser UUID): ${value}`);
  }
}

export function validateDocumentType(value: string): typeof DOCUMENT_TYPES[number] {
  try {
    return DocumentTypeSchema.parse(value);
  } catch {
    throw new Error(`Tipo de documento inválido: ${value}. Valores válidos: ${DOCUMENT_TYPES.join(', ')}`);
  }
}

export function validateVehicleCategory(value: string): typeof VEHICLE_CATEGORIES[number] {
  try {
    return VehicleCategorySchema.parse(value);
  } catch {
    throw new Error(`Categoría de vehículo inválida: ${value}. Valores válidos: ${VEHICLE_CATEGORIES.join(', ')}`);
  }
}

export interface FileValidationResult {
  validFiles: string[];
  errors: string[];
}

export function validateFiles(filePaths: string[]): FileValidationResult {
  const validFiles: string[] = [];
  const errors: string[] = [];

  for (const filePath of filePaths) {
    // Check if file exists
    if (!existsSync(filePath)) {
      errors.push(`El archivo no existe: ${filePath}`);
      continue;
    }

    // Check if it's a file
    const stats = statSync(filePath);
    if (!stats.isFile()) {
      errors.push(`No es un archivo: ${filePath}`);
      continue;
    }

    // Check file extension
    const ext = extname(filePath).toLowerCase();
    if (!SUPPORTED_EXTENSIONS.includes(ext)) {
      errors.push(`Tipo de archivo no soportado: ${filePath} (${ext})`);
      continue;
    }

    // Check file size
    if (stats.size > MAX_FILE_SIZE) {
      const sizeMB = (stats.size / (1024 * 1024)).toFixed(2);
      errors.push(`Archivo demasiado grande: ${filePath} (${sizeMB}MB, máximo 30MB)`);
      continue;
    }

    validFiles.push(filePath);
  }

  return { validFiles, errors };
}

export function hasValidFiles(result: FileValidationResult): boolean {
  return result.validFiles.length > 0;
}

export function hasErrors(result: FileValidationResult): boolean {
  return result.errors.length > 0;
}