export interface IDocumentType {
  id?: number;
  documentTypeCode?: string;
  documentType?: string;
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public documentTypeCode?: string, public documentType?: string) {}
}
