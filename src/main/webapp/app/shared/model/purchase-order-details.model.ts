import { IItems } from 'app/shared/model/items.model';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';

export interface IPurchaseOrderDetails {
  id?: number;
  itemQty?: number;
  item?: IItems;
  po?: IPurchaseOrder;
}

export class PurchaseOrderDetails implements IPurchaseOrderDetails {
  constructor(public id?: number, public itemQty?: number, public item?: IItems, public po?: IPurchaseOrder) {}
}
