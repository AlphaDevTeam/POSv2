import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IDesings } from 'app/shared/model/desings.model';
import { AccountService } from 'app/core/auth/account.service';
import { DesingsService } from './desings.service';

@Component({
  selector: 'jhi-desings',
  templateUrl: './desings.component.html'
})
export class DesingsComponent implements OnInit, OnDestroy {
  desings: IDesings[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected desingsService: DesingsService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.desingsService
      .query()
      .pipe(
        filter((res: HttpResponse<IDesings[]>) => res.ok),
        map((res: HttpResponse<IDesings[]>) => res.body)
      )
      .subscribe((res: IDesings[]) => {
        this.desings = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDesings();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDesings) {
    return item.id;
  }

  registerChangeInDesings() {
    this.eventSubscriber = this.eventManager.subscribe('desingsListModification', response => this.loadAll());
  }
}
