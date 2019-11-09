import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IWorker } from 'app/shared/model/worker.model';
import { AccountService } from 'app/core/auth/account.service';
import { WorkerService } from './worker.service';

@Component({
  selector: 'jhi-worker',
  templateUrl: './worker.component.html'
})
export class WorkerComponent implements OnInit, OnDestroy {
  workers: IWorker[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected workerService: WorkerService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.workerService
      .query()
      .pipe(
        filter((res: HttpResponse<IWorker[]>) => res.ok),
        map((res: HttpResponse<IWorker[]>) => res.body)
      )
      .subscribe((res: IWorker[]) => {
        this.workers = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInWorkers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IWorker) {
    return item.id;
  }

  registerChangeInWorkers() {
    this.eventSubscriber = this.eventManager.subscribe('workerListModification', response => this.loadAll());
  }
}
