import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobDetais } from 'app/shared/model/job-detais.model';
import { JobDetaisService } from './job-detais.service';

@Component({
  selector: 'jhi-job-detais-delete-dialog',
  templateUrl: './job-detais-delete-dialog.component.html'
})
export class JobDetaisDeleteDialogComponent {
  jobDetais: IJobDetais;

  constructor(protected jobDetaisService: JobDetaisService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.jobDetaisService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'jobDetaisListModification',
        content: 'Deleted an jobDetais'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-job-detais-delete-popup',
  template: ''
})
export class JobDetaisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ jobDetais }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(JobDetaisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.jobDetais = jobDetais;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/job-detais', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/job-detais', { outlets: { popup: null } }]);
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
