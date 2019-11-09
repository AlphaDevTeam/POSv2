import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IStock } from 'app/shared/model/stock.model';
import { AccountService } from 'app/core/auth/account.service';
import { StockService } from './stock.service';

@Component({
  selector: 'jhi-stock',
  templateUrl: './stock.component.html'
})
export class StockComponent implements OnInit, OnDestroy {
  stocks: IStock[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected stockService: StockService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.stockService
      .query()
      .pipe(
        filter((res: HttpResponse<IStock[]>) => res.ok),
        map((res: HttpResponse<IStock[]>) => res.body)
      )
      .subscribe((res: IStock[]) => {
        this.stocks = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInStocks();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IStock) {
    return item.id;
  }

  registerChangeInStocks() {
    this.eventSubscriber = this.eventManager.subscribe('stockListModification', response => this.loadAll());
  }
}
