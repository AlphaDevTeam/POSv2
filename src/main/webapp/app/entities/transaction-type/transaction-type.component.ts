import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { AccountService } from 'app/core/auth/account.service';
import { TransactionTypeService } from './transaction-type.service';

@Component({
  selector: 'jhi-transaction-type',
  templateUrl: './transaction-type.component.html'
})
export class TransactionTypeComponent implements OnInit, OnDestroy {
  transactionTypes: ITransactionType[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected transactionTypeService: TransactionTypeService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.transactionTypeService
      .query()
      .pipe(
        filter((res: HttpResponse<ITransactionType[]>) => res.ok),
        map((res: HttpResponse<ITransactionType[]>) => res.body)
      )
      .subscribe((res: ITransactionType[]) => {
        this.transactionTypes = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTransactionTypes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITransactionType) {
    return item.id;
  }

  registerChangeInTransactionTypes() {
    this.eventSubscriber = this.eventManager.subscribe('transactionTypeListModification', response => this.loadAll());
  }
}
