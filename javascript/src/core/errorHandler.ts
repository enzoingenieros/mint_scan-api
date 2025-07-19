import type { ErrorCode, ErrorResponse } from '../models/types.ts';

export class MintApiError extends Error {
  constructor(
    message: string,
    public statusCode?: number,
    public errorCode?: ErrorCode,
    public detail?: string
  ) {
    super(message);
    this.name = 'MintApiError';
  }
}

export function handleApiError(error: unknown): never {
  if (error instanceof MintApiError) {
    console.error(`Error: ${error.message}`);
    if (error.detail) {
      console.error(`Detalle: ${error.detail}`);
    }
    process.exit(1);
  }

  if (error instanceof Error) {
    console.error(`Error: ${error.message}`);
    process.exit(1);
  }

  console.error('Error: Error desconocido');
  process.exit(1);
}

export function translateErrorCode(statusCode: number, errorCode?: ErrorCode): string {
  // Authentication errors
  if (statusCode === 401) {
    switch (errorCode) {
      case 'INVALID_CREDENTIALS':
        return 'Credenciales inválidas';
      case 'TOKEN_NOT_PROVIDED':
        return 'Token no proporcionado';
      case 'INVALID_TOKEN':
        return 'Token inválido o expirado';
      case 'EXPIRED_TOKEN':
        return 'Token expirado';
      default:
        return 'Token inválido o expirado';
    }
  }

  // Forbidden
  if (statusCode === 403) {
    return 'Acceso denegado';
  }

  // Not found
  if (statusCode === 404) {
    return 'Servicio no disponible';
  }

  // Business logic errors
  if (statusCode === 422) {
    switch (errorCode) {
      case 'INVALID_FORMAT':
        return 'Formato de solicitud inválido';
      case 'DOCUMENT_NOT_FOUND':
        return 'Documento no encontrado';
      case 'DOCUMENT_NOT_SAME_STATION':
        return 'Documento no disponible para esta estación';
      case 'DOCUMENT_NOT_SAME_CUSTOMER':
        return 'Documento no disponible para este cliente';
      case 'DOCUMENT_NOT_PROCESSED':
        return 'Documento aún no procesado';
      default:
        return 'Datos de entrada inválidos';
    }
  }

  // Server errors
  if (statusCode >= 500) {
    return 'Error del servidor';
  }

  return `Error de comunicación (código: ${statusCode})`;
}

export function parseErrorResponse(body: string): ErrorResponse | null {
  try {
    return JSON.parse(body) as ErrorResponse;
  } catch {
    return null;
  }
}