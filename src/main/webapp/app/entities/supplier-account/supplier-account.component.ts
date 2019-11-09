import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ISupplierAccount } from 'app/shared/model/supplier-account.model';
import { AccountService } from 'app/core/auth/account.service';
import { SupplierAccountService } from './supplier-account.service';

@Component({
  selector: 'jhi-supplier-account',
  templateUrl: './supplier-account.component.html'
})
export class SupplierAccountComponent implements OnInit, OnDestroy {
  supplierAccounts: ISupplierAccount[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected supplierAccountService: SupplierAccountService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.supplierAccountService
      .query()
      .pipe(
        filter((res: HttpResponse<ISupplierAccount[]>) => res.ok),
        map((res: HttpResponse<ISupplierAccount[]>) => res.body)
      )
      .subscribe((res: ISupplierAccount[]) => {
        this.supplierAccounts = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSupplierAccounts();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISupplierAccount) {
    return item.id;
  }

  registerChangeInSupplierAccounts() {
    this.eventSubscriber = this.eventManager.subscribe('supplierAccountListModification', response => this.loadAll());
  }
}
