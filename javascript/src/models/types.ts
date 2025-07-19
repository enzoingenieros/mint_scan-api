// API Response Types

export interface LoginResponse {
  token: string;
}

export interface ErrorResponse {
  title: string;
  detail: string;
  code: ErrorCode;
  status: number;
}

export type ErrorCode =
  | 'NOT_AUTHORIZED'
  | 'TOKEN_NOT_PROVIDED'
  | 'INVALID_TOKEN'
  | 'INVALID_SIGNATURE'
  | 'EXPIRED_TOKEN'
  | 'INVALID_CREDENTIALS'
  | 'INVALID_FORMAT'
  | 'INTERNAL_SERVER_ERROR'
  | 'CONFLICT_ERROR'
  | 'DOCUMENT_NOT_FOUND'
  | 'DOCUMENT_NOT_SAME_STATION'
  | 'DOCUMENT_NOT_SAME_CUSTOMER'
  | 'DOCUMENT_NOT_PROCESSED';

// Process Types
export type DocumentType = 
  | 'coc' 
  | 'titv-old' 
  | 'titv-new' 
  | 'reduced' 
  | 'single-approval' 
  | 'cdc';

export type VehicleCategory = 
  | 'M1' 
  | 'M3' 
  | 'N1' 
  | 'N3' 
  | 'L' 
  | 'O' 
  | 'T' 
  | 'TR' 
  | 'OS' 
  | 'OSR';

export type ProcessStatus = 
  | 'PENDING' 
  | 'STRAIGHTENING' 
  | 'RECOGNIZING' 
  | 'COMPLETED' 
  | 'FAILED' 
  | 'RETRIEVED' 
  | 'ABORTED';

export type FileType = 
  | 'image/jpeg' 
  | 'application/pdf' 
  | 'image/tiff' 
  | 'image/png';

// Request Types
export interface LoginRequest {
  username: string;
  password: string;
}

export interface ImageObject {
  base64: string;
  fileName: string;
  fileType: FileType;
}

export interface ProcessPoolRequest {
  id: string;
  type: DocumentType;
  category: VehicleCategory;
  images: ImageObject | ImageObject[];
  name?: string;
  extractAccuracy?: boolean;
}

export interface ProcessPoolResponse {
  success: boolean;
  id: string;
  message: string;
}

// List Process Types
export interface ProcessLicense {
  id: string;
  code: string;
  customerId: string;
  itv: string;
}

export interface ProcessTechnicalCard {
  type: DocumentType | 'manual';
  category: VehicleCategory;
  model: string;
}

export interface ProcessDocumentItem {
  id: string;
  license: ProcessLicense;
  technicalCard: ProcessTechnicalCard;
  status: ProcessStatus;
  documentId: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ListProcessResponse {
  processDocuments: ProcessDocumentItem[];
}

// Retrieve Process Types
export interface TechnicalCardData {
  matricula?: string | null;
  certificado?: string | null;
  'A.1'?: string | null;
  'A.2'?: string | null;
  'B.1'?: string | null;
  'B.2'?: string | null;
  'D.1'?: string | null;
  'D.2'?: string | null;
  'D.3'?: string | null;
  'D.6'?: string | null;
  'E'?: string | null;
  'J'?: string | null;
  'J.1'?: string | null;
  'J.2'?: string | null;
  'J.3'?: string | null;
  'R'?: string | null;
  'K'?: string | null;
  'K.1'?: string | null;
  'K.2'?: string | null;
  'Z'?: string | null;
  'G'?: string | null;
  'G.1'?: string | null;
  'F.1'?: string | null;
  'F.1.1'?: string | null;
  'F.1.5'?: string | null;
  'F.2'?: string | null;
  'F.2.1'?: string | null;
  'F.3'?: string | null;
  'F.3.1'?: string | null;
  'F.4'?: string | null;
  'F.5'?: string | null;
  'F.5.1'?: string | null;
  'F.6'?: string | null;
  'F.7'?: string | null;
  'F.7.1'?: string | null;
  'F.8'?: string | null;
  'O.1'?: string | null;
  'O.1.1'?: string | null;
  'O.1.2'?: string | null;
  'O.1.3'?: string | null;
  'O.1.4'?: string | null;
  'O.2.1'?: string | null;
  'O.2.2'?: string | null;
  'O.2.3'?: string | null;
  'O.3'?: string | null;
  'M.1'?: string | null;
  'M.4'?: string | null;
  'L'?: string | null;
  'L.0'?: string | null;
  'L.1'?: string | null;
  'L.2'?: string | null;
  'P.5.1'?: string | null;
  'P.5'?: string | null;
  'P.3'?: string | null;
  'P.1'?: string | null;
  'P.1.1'?: string | null;
  'P.2'?: string | null;
  'P.2.1'?: string | null;
  'S.1'?: string | null;
  'S.2'?: string | null;
  'S.1.1'?: string | null;
  'U.1'?: string | null;
  'U.2'?: string | null;
  'V.7'?: string | null;
  'V.8'?: string | null;
  'V.9'?: string | null;
  'Q'?: string | null;
  'EP'?: string | null;
  'EP.1'?: string | null;
  'EP.2'?: string | null;
  'EP.3'?: string | null;
  'EP.4'?: string | null;
  'T'?: string | null;
  numEjes?: string | null;
  numRuedas?: string | null;
  numNeumaticos?: string | null;
  homologaciones?: string[] | null;
  observaciones?: string[] | null;
  reformas?: string[] | null;
  fechaEmision?: string | null;
}

export interface TechnicalCard {
  type: DocumentType | 'manual';
  category: VehicleCategory;
  model: string;
  vehicleLicense: string | null;
  vin: string | null;
  ict: boolean | null;
  data: TechnicalCardData;
}

export interface ProcessedDocument {
  id: string;
  documentId: string | null;
  license: ProcessLicense;
  technicalCard: TechnicalCard;
  status: ProcessStatus;
  createdAt: string;
  updatedAt: string;
}