import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalesAccount } from 'app/shared/model/sales-account.model';

@Component({
  selector: 'jhi-sales-account-detail',
  templateUrl: './sales-account-detail.component.html'
})
export class SalesAccountDetailComponent implements OnInit {
  salesAccount: ISalesAccount;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ salesAccount }) => {
      this.salesAccount = salesAccount;
    });
  }

  previousState() {
    window.history.back();
  }
}
