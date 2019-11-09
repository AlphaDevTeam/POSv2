export interface ISupplierAccountBalance {
  id?: number;
  balance?: number;
}

export class SupplierAccountBalance implements ISupplierAccountBalance {
  constructor(public id?: number, public balance?: number) {}
}
