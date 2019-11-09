import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerAccount } from 'app/shared/model/customer-account.model';

@Component({
  selector: 'jhi-customer-account-detail',
  templateUrl: './customer-account-detail.component.html'
})
export class CustomerAccountDetailComponent implements OnInit {
  customerAccount: ICustomerAccount;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customerAccount }) => {
      this.customerAccount = customerAccount;
    });
  }

  previousState() {
    window.history.back();
  }
}
