import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ISalesAccountBalance } from 'app/shared/model/sales-account-balance.model';
import { AccountService } from 'app/core/auth/account.service';
import { SalesAccountBalanceService } from './sales-account-balance.service';

@Component({
  selector: 'jhi-sales-account-balance',
  templateUrl: './sales-account-balance.component.html'
})
export class SalesAccountBalanceComponent implements OnInit, OnDestroy {
  salesAccountBalances: ISalesAccountBalance[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected salesAccountBalanceService: SalesAccountBalanceService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.salesAccountBalanceService
      .query()
      .pipe(
        filter((res: HttpResponse<ISalesAccountBalance[]>) => res.ok),
        map((res: HttpResponse<ISalesAccountBalance[]>) => res.body)
      )
      .subscribe((res: ISalesAccountBalance[]) => {
        this.salesAccountBalances = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSalesAccountBalances();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISalesAccountBalance) {
    return item.id;
  }

  registerChangeInSalesAccountBalances() {
    this.eventSubscriber = this.eventManager.subscribe('salesAccountBalanceListModification', response => this.loadAll());
  }
}
