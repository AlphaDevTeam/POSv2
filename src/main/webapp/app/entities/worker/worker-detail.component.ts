import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorker } from 'app/shared/model/worker.model';

@Component({
  selector: 'jhi-worker-detail',
  templateUrl: './worker-detail.component.html'
})
export class WorkerDetailComponent implements OnInit {
  worker: IWorker;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ worker }) => {
      this.worker = worker;
    });
  }

  previousState() {
    window.history.back();
  }
}
