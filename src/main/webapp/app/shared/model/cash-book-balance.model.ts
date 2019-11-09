import { ILocation } from 'app/shared/model/location.model';

export interface ICashBookBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
}

export class CashBookBalance implements ICashBookBalance {
  constructor(public id?: number, public balance?: number, public location?: ILocation) {}
}
