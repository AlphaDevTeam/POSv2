import { ILocation } from 'app/shared/model/location.model';

export interface ICustomerAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
}

export class CustomerAccountBalance implements ICustomerAccountBalance {
  constructor(public id?: number, public balance?: number, public location?: ILocation) {}
}
