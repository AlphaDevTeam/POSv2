import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IDesigns } from 'app/shared/model/designs.model';
import { AccountService } from 'app/core/auth/account.service';
import { DesignsService } from './designs.service';

@Component({
  selector: 'jhi-designs',
  templateUrl: './designs.component.html'
})
export class DesignsComponent implements OnInit, OnDestroy {
  designs: IDesigns[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected designsService: DesignsService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.designsService
      .query()
      .pipe(
        filter((res: HttpResponse<IDesigns[]>) => res.ok),
        map((res: HttpResponse<IDesigns[]>) => res.body)
      )
      .subscribe((res: IDesigns[]) => {
        this.designs = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDesigns();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDesigns) {
    return item.id;
  }

  registerChangeInDesigns() {
    this.eventSubscriber = this.eventManager.subscribe('designsListModification', response => this.loadAll());
  }
}
