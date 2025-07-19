import { ApiClient } from '../client.ts';
import type { 
  ProcessPoolRequest, 
  ProcessPoolResponse, 
  ProcessedDocument,
  ImageObject,
  DocumentType,
  VehicleCategory
} from '../../models/types.ts';
import { createImageObject, processMultipleFiles } from '../../utils/base64.ts';

export class ProcessService {
  private apiClient: ApiClient;

  constructor(apiClient?: ApiClient) {
    this.apiClient = apiClient || new ApiClient();
  }

  async processImagePool(
    token: string,
    processId: string,
    documentType: DocumentType,
    vehicleCategory: VehicleCategory,
    images: ImageObject | ImageObject[],
    name?: string,
    extractAccuracy = false
  ): Promise<ProcessPoolResponse> {
    const request: ProcessPoolRequest = {
      id: processId,
      type: documentType,
      category: vehicleCategory,
      images: Array.isArray(images) ? images : [images],
    };

    if (name) {
      request.name = name.slice(0, 100); // Limit to 100 characters
    }

    if (extractAccuracy) {
      request.extractAccuracy = extractAccuracy;
    }

    return this.apiClient.post<ProcessPoolResponse>('/process/pool', request, token);
  }

  async processSingleImage(
    token: string,
    filePath: string,
    documentType: DocumentType,
    vehicleCategory: VehicleCategory,
    name?: string,
    extractAccuracy = false,
    processId?: string
  ): Promise<ProcessPoolResponse> {
    const image = createImageObject(filePath);
    const id = processId || crypto.randomUUID();
    
    return this.processImagePool(
      token,
      id,
      documentType,
      vehicleCategory,
      image,
      name,
      extractAccuracy
    );
  }

  async processMultipleImages(
    token: string,
    filePaths: string[],
    documentType: DocumentType,
    vehicleCategory: VehicleCategory,
    name?: string,
    extractAccuracy = false,
    processId?: string
  ): Promise<ProcessPoolResponse> {
    const images = processMultipleFiles(filePaths);
    const id = processId || crypto.randomUUID();
    
    return this.processImagePool(
      token,
      id,
      documentType,
      vehicleCategory,
      images,
      name,
      extractAccuracy
    );
  }

  async retrieveProcessedDocument(token: string, processId: string): Promise<ProcessedDocument> {
    return this.apiClient.get<ProcessedDocument>(`/process/${processId}`, token);
  }

  async abortProcess(token: string, processId: string): Promise<{ success: boolean; message: string }> {
    return this.apiClient.post(`/process/pool/abort/${processId}`, {}, token);
  }
}