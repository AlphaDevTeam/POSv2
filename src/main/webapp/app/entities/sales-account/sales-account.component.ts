import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ISalesAccount } from 'app/shared/model/sales-account.model';
import { AccountService } from 'app/core/auth/account.service';
import { SalesAccountService } from './sales-account.service';

@Component({
  selector: 'jhi-sales-account',
  templateUrl: './sales-account.component.html'
})
export class SalesAccountComponent implements OnInit, OnDestroy {
  salesAccounts: ISalesAccount[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected salesAccountService: SalesAccountService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.salesAccountService
      .query()
      .pipe(
        filter((res: HttpResponse<ISalesAccount[]>) => res.ok),
        map((res: HttpResponse<ISalesAccount[]>) => res.body)
      )
      .subscribe((res: ISalesAccount[]) => {
        this.salesAccounts = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSalesAccounts();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISalesAccount) {
    return item.id;
  }

  registerChangeInSalesAccounts() {
    this.eventSubscriber = this.eventManager.subscribe('salesAccountListModification', response => this.loadAll());
  }
}
