import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { AccountService } from 'app/core/auth/account.service';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';

@Component({
  selector: 'jhi-goods-receipt-details',
  templateUrl: './goods-receipt-details.component.html'
})
export class GoodsReceiptDetailsComponent implements OnInit, OnDestroy {
  goodsReceiptDetails: IGoodsReceiptDetails[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.goodsReceiptDetailsService
      .query()
      .pipe(
        filter((res: HttpResponse<IGoodsReceiptDetails[]>) => res.ok),
        map((res: HttpResponse<IGoodsReceiptDetails[]>) => res.body)
      )
      .subscribe((res: IGoodsReceiptDetails[]) => {
        this.goodsReceiptDetails = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInGoodsReceiptDetails();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGoodsReceiptDetails) {
    return item.id;
  }

  registerChangeInGoodsReceiptDetails() {
    this.eventSubscriber = this.eventManager.subscribe('goodsReceiptDetailsListModification', response => this.loadAll());
  }
}
