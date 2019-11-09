import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IItemBinCard } from 'app/shared/model/item-bin-card.model';
import { AccountService } from 'app/core/auth/account.service';
import { ItemBinCardService } from './item-bin-card.service';

@Component({
  selector: 'jhi-item-bin-card',
  templateUrl: './item-bin-card.component.html'
})
export class ItemBinCardComponent implements OnInit, OnDestroy {
  itemBinCards: IItemBinCard[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected itemBinCardService: ItemBinCardService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.itemBinCardService
      .query()
      .pipe(
        filter((res: HttpResponse<IItemBinCard[]>) => res.ok),
        map((res: HttpResponse<IItemBinCard[]>) => res.body)
      )
      .subscribe((res: IItemBinCard[]) => {
        this.itemBinCards = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInItemBinCards();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IItemBinCard) {
    return item.id;
  }

  registerChangeInItemBinCards() {
    this.eventSubscriber = this.eventManager.subscribe('itemBinCardListModification', response => this.loadAll());
  }
}
