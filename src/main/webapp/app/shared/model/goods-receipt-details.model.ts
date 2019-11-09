import { IItems } from 'app/shared/model/items.model';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';

export interface IGoodsReceiptDetails {
  id?: number;
  grnQty?: string;
  item?: IItems;
  grn?: IGoodsReceipt;
}

export class GoodsReceiptDetails implements IGoodsReceiptDetails {
  constructor(public id?: number, public grnQty?: string, public item?: IItems, public grn?: IGoodsReceipt) {}
}
