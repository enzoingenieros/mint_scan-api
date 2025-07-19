import { API_BASE_URL } from '../models/constants.ts';
import { MintApiError, parseErrorResponse, translateErrorCode } from '../core/errorHandler.ts';

interface RequestOptions {
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  headers?: Record<string, string>;
  body?: unknown;
}

export class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = API_BASE_URL) {
    this.baseUrl = baseUrl;
  }

  private createHeaders(token?: string | null, additionalHeaders?: Record<string, string>): Record<string, string> {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...additionalHeaders,
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    return headers;
  }

  async request<T>(endpoint: string, options: RequestOptions): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;
    
    try {
      const fetchOptions: RequestInit = {
        method: options.method,
        body: options.body ? JSON.stringify(options.body) : undefined,
      };
      
      if (options.headers) {
        fetchOptions.headers = options.headers;
      }
      
      const response = await fetch(url, fetchOptions);

      const responseBody = await response.text();

      if (!response.ok) {
        const errorResponse = parseErrorResponse(responseBody);
        const message = translateErrorCode(response.status, errorResponse?.code);
        
        throw new MintApiError(
          message,
          response.status,
          errorResponse?.code,
          errorResponse?.detail
        );
      }

      if (!responseBody) {
        return {} as T;
      }

      return JSON.parse(responseBody) as T;
    } catch (error) {
      if (error instanceof MintApiError) {
        throw error;
      }

      if (error instanceof Error) {
        if (error.message.includes('fetch failed')) {
          throw new MintApiError('No se pudo conectar al servidor');
        }
        if (error.message.includes('timed out')) {
          throw new MintApiError('Tiempo de espera agotado');
        }
        if (error.message.includes('connection')) {
          throw new MintApiError('Error de conexión');
        }
      }

      throw new MintApiError('Error durante la comunicación con el servidor');
    }
  }

  async get<T>(endpoint: string, token?: string | null): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'GET',
      headers: this.createHeaders(token),
    });
  }

  async post<T>(endpoint: string, body: unknown, token?: string | null): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      headers: this.createHeaders(token),
      body,
    });
  }
}

export function createBearerHeader(token: string): Record<string, string> {
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json',
  };
}