import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';

@Component({
  selector: 'jhi-supplier-account-balance-detail',
  templateUrl: './supplier-account-balance-detail.component.html'
})
export class SupplierAccountBalanceDetailComponent implements OnInit {
  supplierAccountBalance: ISupplierAccountBalance;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ supplierAccountBalance }) => {
      this.supplierAccountBalance = supplierAccountBalance;
    });
  }

  previousState() {
    window.history.back();
  }
}
