import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashBookBalance } from 'app/shared/model/cash-book-balance.model';

@Component({
  selector: 'jhi-cash-book-balance-detail',
  templateUrl: './cash-book-balance-detail.component.html'
})
export class CashBookBalanceDetailComponent implements OnInit {
  cashBookBalance: ICashBookBalance;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cashBookBalance }) => {
      this.cashBookBalance = cashBookBalance;
    });
  }

  previousState() {
    window.history.back();
  }
}
