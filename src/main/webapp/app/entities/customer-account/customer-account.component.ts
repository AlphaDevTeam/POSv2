import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerAccount } from 'app/shared/model/customer-account.model';
import { AccountService } from 'app/core/auth/account.service';
import { CustomerAccountService } from './customer-account.service';

@Component({
  selector: 'jhi-customer-account',
  templateUrl: './customer-account.component.html'
})
export class CustomerAccountComponent implements OnInit, OnDestroy {
  customerAccounts: ICustomerAccount[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected customerAccountService: CustomerAccountService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.customerAccountService
      .query()
      .pipe(
        filter((res: HttpResponse<ICustomerAccount[]>) => res.ok),
        map((res: HttpResponse<ICustomerAccount[]>) => res.body)
      )
      .subscribe((res: ICustomerAccount[]) => {
        this.customerAccounts = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCustomerAccounts();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICustomerAccount) {
    return item.id;
  }

  registerChangeInCustomerAccounts() {
    this.eventSubscriber = this.eventManager.subscribe('customerAccountListModification', response => this.loadAll());
  }
}
