import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesings } from 'app/shared/model/desings.model';
import { DesingsService } from './desings.service';

@Component({
  selector: 'jhi-desings-delete-dialog',
  templateUrl: './desings-delete-dialog.component.html'
})
export class DesingsDeleteDialogComponent {
  desings: IDesings;

  constructor(protected desingsService: DesingsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.desingsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'desingsListModification',
        content: 'Deleted an desings'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-desings-delete-popup',
  template: ''
})
export class DesingsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ desings }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DesingsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.desings = desings;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/desings', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/desings', { outlets: { popup: null } }]);
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
