import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

@Component({
  selector: 'jhi-goods-receipt-details-detail',
  templateUrl: './goods-receipt-details-detail.component.html'
})
export class GoodsReceiptDetailsDetailComponent implements OnInit {
  goodsReceiptDetails: IGoodsReceiptDetails;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ goodsReceiptDetails }) => {
      this.goodsReceiptDetails = goodsReceiptDetails;
    });
  }

  previousState() {
    window.history.back();
  }
}
