import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

@Component({
  selector: 'jhi-sales-account-balance-detail',
  templateUrl: './sales-account-balance-detail.component.html'
})
export class SalesAccountBalanceDetailComponent implements OnInit {
  salesAccountBalance: ISalesAccountBalance;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ salesAccountBalance }) => {
      this.salesAccountBalance = salesAccountBalance;
    });
  }

  previousState() {
    window.history.back();
  }
}
