import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IJobDetais } from 'app/shared/model/job-detais.model';
import { AccountService } from 'app/core/auth/account.service';
import { JobDetaisService } from './job-detais.service';

@Component({
  selector: 'jhi-job-detais',
  templateUrl: './job-detais.component.html'
})
export class JobDetaisComponent implements OnInit, OnDestroy {
  jobDetais: IJobDetais[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected jobDetaisService: JobDetaisService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.jobDetaisService
      .query()
      .pipe(
        filter((res: HttpResponse<IJobDetais[]>) => res.ok),
        map((res: HttpResponse<IJobDetais[]>) => res.body)
      )
      .subscribe((res: IJobDetais[]) => {
        this.jobDetais = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInJobDetais();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IJobDetais) {
    return item.id;
  }

  registerChangeInJobDetais() {
    this.eventSubscriber = this.eventManager.subscribe('jobDetaisListModification', response => this.loadAll());
  }
}
