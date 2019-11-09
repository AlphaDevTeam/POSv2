import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorker } from 'app/shared/model/worker.model';
import { WorkerService } from './worker.service';

@Component({
  selector: 'jhi-worker-delete-dialog',
  templateUrl: './worker-delete-dialog.component.html'
})
export class WorkerDeleteDialogComponent {
  worker: IWorker;

  constructor(protected workerService: WorkerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.workerService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'workerListModification',
        content: 'Deleted an worker'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-worker-delete-popup',
  template: ''
})
export class WorkerDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ worker }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(WorkerDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.worker = worker;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/worker', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/worker', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
