import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';
import { AccountService } from 'app/core/auth/account.service';
import { PurchaseAccountBalanceService } from './purchase-account-balance.service';

@Component({
  selector: 'jhi-purchase-account-balance',
  templateUrl: './purchase-account-balance.component.html'
})
export class PurchaseAccountBalanceComponent implements OnInit, OnDestroy {
  purchaseAccountBalances: IPurchaseAccountBalance[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected purchaseAccountBalanceService: PurchaseAccountBalanceService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.purchaseAccountBalanceService
      .query()
      .pipe(
        filter((res: HttpResponse<IPurchaseAccountBalance[]>) => res.ok),
        map((res: HttpResponse<IPurchaseAccountBalance[]>) => res.body)
      )
      .subscribe((res: IPurchaseAccountBalance[]) => {
        this.purchaseAccountBalances = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPurchaseAccountBalances();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPurchaseAccountBalance) {
    return item.id;
  }

  registerChangeInPurchaseAccountBalances() {
    this.eventSubscriber = this.eventManager.subscribe('purchaseAccountBalanceListModification', response => this.loadAll());
  }
}
