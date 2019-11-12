import { IProducts } from 'app/shared/model/products.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IDesigns {
  id?: number;
  designCode?: string;
  designName?: string;
  designPrefix?: string;
  designProfMargin?: number;
  relatedProduct?: IProducts;
  location?: ILocation;
}

export class Designs implements IDesigns {
  constructor(
    public id?: number,
    public designCode?: string,
    public designName?: string,
    public designPrefix?: string,
    public designProfMargin?: number,
    public relatedProduct?: IProducts,
    public location?: ILocation
  ) {}
}
