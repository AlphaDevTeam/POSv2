import { ILocation } from 'app/shared/model/location.model';

export interface ISupplier {
  id?: number;
  supplierCode?: string;
  supplierName?: string;
  supplierLimit?: number;
  isActive?: boolean;
  location?: ILocation;
}

export class Supplier implements ISupplier {
  constructor(
    public id?: number,
    public supplierCode?: string,
    public supplierName?: string,
    public supplierLimit?: number,
    public isActive?: boolean,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}
