import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';

@Component({
  selector: 'jhi-goods-receipt-detail',
  templateUrl: './goods-receipt-detail.component.html'
})
export class GoodsReceiptDetailComponent implements OnInit {
  goodsReceipt: IGoodsReceipt;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ goodsReceipt }) => {
      this.goodsReceipt = goodsReceipt;
    });
  }

  previousState() {
    window.history.back();
  }
}
