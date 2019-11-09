import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobStatus } from 'app/shared/model/job-status.model';
import { JobStatusService } from './job-status.service';

@Component({
  selector: 'jhi-job-status-delete-dialog',
  templateUrl: './job-status-delete-dialog.component.html'
})
export class JobStatusDeleteDialogComponent {
  jobStatus: IJobStatus;

  constructor(protected jobStatusService: JobStatusService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.jobStatusService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'jobStatusListModification',
        content: 'Deleted an jobStatus'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-job-status-delete-popup',
  template: ''
})
export class JobStatusDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ jobStatus }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(JobStatusDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.jobStatus = jobStatus;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/job-status', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/job-status', { outlets: { popup: null } }]);
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
