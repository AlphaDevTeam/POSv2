import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';
import { AccountService } from 'app/core/auth/account.service';
import { CustomerAccountBalanceService } from './customer-account-balance.service';

@Component({
  selector: 'jhi-customer-account-balance',
  templateUrl: './customer-account-balance.component.html'
})
export class CustomerAccountBalanceComponent implements OnInit, OnDestroy {
  customerAccountBalances: ICustomerAccountBalance[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected customerAccountBalanceService: CustomerAccountBalanceService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.customerAccountBalanceService
      .query()
      .pipe(
        filter((res: HttpResponse<ICustomerAccountBalance[]>) => res.ok),
        map((res: HttpResponse<ICustomerAccountBalance[]>) => res.body)
      )
      .subscribe((res: ICustomerAccountBalance[]) => {
        this.customerAccountBalances = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCustomerAccountBalances();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICustomerAccountBalance) {
    return item.id;
  }

  registerChangeInCustomerAccountBalances() {
    this.eventSubscriber = this.eventManager.subscribe('customerAccountBalanceListModification', response => this.loadAll());
  }
}
