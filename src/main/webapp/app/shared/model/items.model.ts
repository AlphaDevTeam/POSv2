import { Moment } from 'moment';
import { IDesigns } from 'app/shared/model/designs.model';
import { IProducts } from 'app/shared/model/products.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IItems {
  id?: number;
  itemCode?: string;
  itemName?: string;
  itemDescription?: string;
  itemPrice?: number;
  itemSerial?: string;
  itemSupplierSerial?: string;
  itemCost?: number;
  itemSalePrice?: number;
  originalStockDate?: Moment;
  modifiedStockDate?: Moment;
  relatedDesign?: IDesigns;
  relatedProduct?: IProducts;
  location?: ILocation;
}

export class Items implements IItems {
  constructor(
    public id?: number,
    public itemCode?: string,
    public itemName?: string,
    public itemDescription?: string,
    public itemPrice?: number,
    public itemSerial?: string,
    public itemSupplierSerial?: string,
    public itemCost?: number,
    public itemSalePrice?: number,
    public originalStockDate?: Moment,
    public modifiedStockDate?: Moment,
    public relatedDesign?: IDesigns,
    public relatedProduct?: IProducts,
    public location?: ILocation
  ) {}
}
