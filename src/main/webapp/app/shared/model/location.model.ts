import { ICompany } from 'app/shared/model/company.model';
import { IExUser } from 'app/shared/model/ex-user.model';

export interface ILocation {
  id?: number;
  locationCode?: string;
  locationName?: string;
  locationProfMargin?: number;
  isActive?: boolean;
  company?: ICompany;
  users?: IExUser[];
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public locationCode?: string,
    public locationName?: string,
    public locationProfMargin?: number,
    public isActive?: boolean,
    public company?: ICompany,
    public users?: IExUser[]
  ) {
    this.isActive = this.isActive || false;
  }
}
