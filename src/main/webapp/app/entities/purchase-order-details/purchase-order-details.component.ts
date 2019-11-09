import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';
import { AccountService } from 'app/core/auth/account.service';
import { PurchaseOrderDetailsService } from './purchase-order-details.service';

@Component({
  selector: 'jhi-purchase-order-details',
  templateUrl: './purchase-order-details.component.html'
})
export class PurchaseOrderDetailsComponent implements OnInit, OnDestroy {
  purchaseOrderDetails: IPurchaseOrderDetails[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected purchaseOrderDetailsService: PurchaseOrderDetailsService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.purchaseOrderDetailsService
      .query()
      .pipe(
        filter((res: HttpResponse<IPurchaseOrderDetails[]>) => res.ok),
        map((res: HttpResponse<IPurchaseOrderDetails[]>) => res.body)
      )
      .subscribe((res: IPurchaseOrderDetails[]) => {
        this.purchaseOrderDetails = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPurchaseOrderDetails();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPurchaseOrderDetails) {
    return item.id;
  }

  registerChangeInPurchaseOrderDetails() {
    this.eventSubscriber = this.eventManager.subscribe('purchaseOrderDetailsListModification', response => this.loadAll());
  }
}
