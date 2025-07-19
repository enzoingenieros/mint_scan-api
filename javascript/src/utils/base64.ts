import { readFileSync } from 'node:fs';
import { basename, extname } from 'node:path';
import { FILE_TYPE_MAP } from '../models/constants.ts';
import type { ImageObject, FileType } from '../models/types.ts';

export function encodeFileToBase64(filePath: string): string {
  const fileBuffer = readFileSync(filePath);
  return fileBuffer.toString('base64');
}

export function createImageObject(filePath: string, customFileName?: string): ImageObject {
  const fileName = customFileName || basename(filePath);
  const ext = extname(filePath).toLowerCase();
  const fileType = FILE_TYPE_MAP[ext];
  
  if (!fileType) {
    throw new Error(`Tipo de archivo no soportado: ${ext}`);
  }

  return {
    base64: encodeFileToBase64(filePath),
    fileName,
    fileType: fileType as FileType,
  };
}

export function processMultipleFiles(filePaths: string[]): ImageObject[] {
  const images: ImageObject[] = [];
  
  for (const filePath of filePaths) {
    try {
      const imageObject = createImageObject(filePath);
      images.push(imageObject);
      console.log(`Añadido: ${filePath}`);
    } catch (error) {
      console.error(`Error procesando ${filePath}: ${error instanceof Error ? error.message : 'Error desconocido'}`);
    }
  }
  
  if (images.length === 0) {
    throw new Error('No hay imágenes válidas para procesar');
  }
  
  return images;
}