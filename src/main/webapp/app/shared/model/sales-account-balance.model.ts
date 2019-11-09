import { ILocation } from 'app/shared/model/location.model';

export interface ISalesAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
}

export class SalesAccountBalance implements ISalesAccountBalance {
  constructor(public id?: number, public balance?: number, public location?: ILocation) {}
}
