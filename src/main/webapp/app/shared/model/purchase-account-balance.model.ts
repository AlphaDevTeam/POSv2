import { ILocation } from 'app/shared/model/location.model';

export interface IPurchaseAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
}

export class PurchaseAccountBalance implements IPurchaseAccountBalance {
  constructor(public id?: number, public balance?: number, public location?: ILocation) {}
}
