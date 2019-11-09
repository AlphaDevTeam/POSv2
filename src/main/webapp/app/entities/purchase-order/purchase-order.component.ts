import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { AccountService } from 'app/core/auth/account.service';
import { PurchaseOrderService } from './purchase-order.service';

@Component({
  selector: 'jhi-purchase-order',
  templateUrl: './purchase-order.component.html'
})
export class PurchaseOrderComponent implements OnInit, OnDestroy {
  purchaseOrders: IPurchaseOrder[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected purchaseOrderService: PurchaseOrderService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.purchaseOrderService
      .query()
      .pipe(
        filter((res: HttpResponse<IPurchaseOrder[]>) => res.ok),
        map((res: HttpResponse<IPurchaseOrder[]>) => res.body)
      )
      .subscribe((res: IPurchaseOrder[]) => {
        this.purchaseOrders = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPurchaseOrders();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPurchaseOrder) {
    return item.id;
  }

  registerChangeInPurchaseOrders() {
    this.eventSubscriber = this.eventManager.subscribe('purchaseOrderListModification', response => this.loadAll());
  }
}
