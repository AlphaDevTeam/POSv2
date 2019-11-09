import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';

@Component({
  selector: 'jhi-purchase-account-balance-detail',
  templateUrl: './purchase-account-balance-detail.component.html'
})
export class PurchaseAccountBalanceDetailComponent implements OnInit {
  purchaseAccountBalance: IPurchaseAccountBalance;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ purchaseAccountBalance }) => {
      this.purchaseAccountBalance = purchaseAccountBalance;
    });
  }

  previousState() {
    window.history.back();
  }
}
