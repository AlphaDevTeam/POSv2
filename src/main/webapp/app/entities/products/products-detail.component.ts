import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProducts } from 'app/shared/model/products.model';

@Component({
  selector: 'jhi-products-detail',
  templateUrl: './products-detail.component.html'
})
export class ProductsDetailComponent implements OnInit {
  products: IProducts;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ products }) => {
      this.products = products;
    });
  }

  previousState() {
    window.history.back();
  }
}
