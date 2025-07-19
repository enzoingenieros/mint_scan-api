import { ApiClient } from '../client.ts';
import type { 
  ListProcessResponse, 
  ProcessDocumentItem,
  ProcessStatus,
  DocumentType,
  VehicleCategory
} from '../../models/types.ts';

export interface DocumentStatistics {
  total: number;
  statusCounts: Record<ProcessStatus, number>;
  typeCounts: Record<DocumentType | 'manual', number>;
  itvCounts: Record<string, number>;
}

export class ListService {
  private apiClient: ApiClient;

  constructor(apiClient?: ApiClient) {
    this.apiClient = apiClient || new ApiClient();
  }

  async listProcessedDocuments(token: string): Promise<ProcessDocumentItem[]> {
    const response = await this.apiClient.get<ListProcessResponse>('/process', token);
    return response.processDocuments || [];
  }

  filterByStatus(documents: ProcessDocumentItem[], status: ProcessStatus): ProcessDocumentItem[] {
    return documents.filter(doc => doc.status === status);
  }

  filterByType(documents: ProcessDocumentItem[], type: DocumentType): ProcessDocumentItem[] {
    return documents.filter(doc => doc.technicalCard.type === type);
  }

  filterByCategory(documents: ProcessDocumentItem[], category: VehicleCategory): ProcessDocumentItem[] {
    return documents.filter(doc => doc.technicalCard.category === category);
  }

  filterByItv(documents: ProcessDocumentItem[], itv: string): ProcessDocumentItem[] {
    return documents.filter(doc => doc.license.itv === itv);
  }

  sortByDate(
    documents: ProcessDocumentItem[], 
    useCreatedAt = true, 
    descending = true
  ): ProcessDocumentItem[] {
    return [...documents].sort((a, b) => {
      const dateA = new Date(useCreatedAt ? a.createdAt : a.updatedAt);
      const dateB = new Date(useCreatedAt ? b.createdAt : b.updatedAt);
      
      if (descending) {
        return dateB.getTime() - dateA.getTime();
      } else {
        return dateA.getTime() - dateB.getTime();
      }
    });
  }

  getStatistics(documents: ProcessDocumentItem[]): DocumentStatistics {
    const stats: DocumentStatistics = {
      total: documents.length,
      statusCounts: {} as Record<ProcessStatus, number>,
      typeCounts: {} as Record<DocumentType | 'manual', number>,
      itvCounts: {} as Record<string, number>,
    };

    for (const doc of documents) {
      // Count by status
      const status = doc.status;
      stats.statusCounts[status] = (stats.statusCounts[status] || 0) + 1;

      // Count by type
      const type = doc.technicalCard.type;
      stats.typeCounts[type] = (stats.typeCounts[type] || 0) + 1;

      // Count by ITV
      const itv = doc.license.itv;
      stats.itvCounts[itv] = (stats.itvCounts[itv] || 0) + 1;
    }

    return stats;
  }
}