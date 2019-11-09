import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IJobStatus } from 'app/shared/model/job-status.model';
import { AccountService } from 'app/core/auth/account.service';
import { JobStatusService } from './job-status.service';

@Component({
  selector: 'jhi-job-status',
  templateUrl: './job-status.component.html'
})
export class JobStatusComponent implements OnInit, OnDestroy {
  jobStatuses: IJobStatus[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected jobStatusService: JobStatusService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.jobStatusService
      .query()
      .pipe(
        filter((res: HttpResponse<IJobStatus[]>) => res.ok),
        map((res: HttpResponse<IJobStatus[]>) => res.body)
      )
      .subscribe((res: IJobStatus[]) => {
        this.jobStatuses = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInJobStatuses();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IJobStatus) {
    return item.id;
  }

  registerChangeInJobStatuses() {
    this.eventSubscriber = this.eventManager.subscribe('jobStatusListModification', response => this.loadAll());
  }
}
