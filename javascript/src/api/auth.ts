import { ApiClient } from './client.ts';
import { MintApiError } from '../core/errorHandler.ts';
import type { LoginRequest, LoginResponse } from '../models/types.ts';

export class AuthService {
  private apiClient: ApiClient;

  constructor(apiClient?: ApiClient) {
    this.apiClient = apiClient || new ApiClient();
  }

  async login(username: string, password: string): Promise<string> {
    if (!username || username.trim() === '') {
      throw new Error('El nombre de usuario es requerido');
    }

    if (!password || password.trim() === '') {
      throw new Error('La contraseña es requerida');
    }

    const request: LoginRequest = { username, password };
    
    try {
      const response = await this.apiClient.post<LoginResponse>('/login', request);
      
      if (!response.token || response.token === '') {
        throw new MintApiError('No hay token en la respuesta');
      }
      
      return response.token;
    } catch (error) {
      if (error instanceof MintApiError) {
        throw error;
      }
      throw new MintApiError('Error durante el inicio de sesión');
    }
  }
}