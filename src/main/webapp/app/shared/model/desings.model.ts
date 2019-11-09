import { IProducts } from 'app/shared/model/products.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IDesings {
  id?: number;
  desingCode?: string;
  desingName?: string;
  desingPrefix?: string;
  desingProfMargin?: number;
  relatedProduct?: IProducts;
  location?: ILocation;
}

export class Desings implements IDesings {
  constructor(
    public id?: number,
    public desingCode?: string,
    public desingName?: string,
    public desingPrefix?: string,
    public desingProfMargin?: number,
    public relatedProduct?: IProducts,
    public location?: ILocation
  ) {}
}
