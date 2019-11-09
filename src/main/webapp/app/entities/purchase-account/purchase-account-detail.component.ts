import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseAccount } from 'app/shared/model/purchase-account.model';

@Component({
  selector: 'jhi-purchase-account-detail',
  templateUrl: './purchase-account-detail.component.html'
})
export class PurchaseAccountDetailComponent implements OnInit {
  purchaseAccount: IPurchaseAccount;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ purchaseAccount }) => {
      this.purchaseAccount = purchaseAccount;
    });
  }

  previousState() {
    window.history.back();
  }
}
