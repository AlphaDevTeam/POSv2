import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';

@Component({
  selector: 'jhi-customer-account-balance-detail',
  templateUrl: './customer-account-balance-detail.component.html'
})
export class CustomerAccountBalanceDetailComponent implements OnInit {
  customerAccountBalance: ICustomerAccountBalance;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customerAccountBalance }) => {
      this.customerAccountBalance = customerAccountBalance;
    });
  }

  previousState() {
    window.history.back();
  }
}
