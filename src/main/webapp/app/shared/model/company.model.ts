export interface ICompany {
  id?: number;
  companyCode?: string;
  companyName?: string;
  companyRegNumber?: string;
}

export class Company implements ICompany {
  constructor(public id?: number, public companyCode?: string, public companyName?: string, public companyRegNumber?: string) {}
}
