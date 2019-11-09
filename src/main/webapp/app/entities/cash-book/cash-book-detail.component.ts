import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashBook } from 'app/shared/model/cash-book.model';

@Component({
  selector: 'jhi-cash-book-detail',
  templateUrl: './cash-book-detail.component.html'
})
export class CashBookDetailComponent implements OnInit {
  cashBook: ICashBook;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cashBook }) => {
      this.cashBook = cashBook;
    });
  }

  previousState() {
    window.history.back();
  }
}
