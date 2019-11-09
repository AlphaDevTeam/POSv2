import { Moment } from 'moment';
import { ILocation } from 'app/shared/model/location.model';
import { IItems } from 'app/shared/model/items.model';

export interface IItemBinCard {
  id?: number;
  transactionDate?: Moment;
  transactionDescription?: string;
  transactionQty?: number;
  transactionBalance?: number;
  location?: ILocation;
  item?: IItems;
}

export class ItemBinCard implements IItemBinCard {
  constructor(
    public id?: number,
    public transactionDate?: Moment,
    public transactionDescription?: string,
    public transactionQty?: number,
    public transactionBalance?: number,
    public location?: ILocation,
    public item?: IItems
  ) {}
}
