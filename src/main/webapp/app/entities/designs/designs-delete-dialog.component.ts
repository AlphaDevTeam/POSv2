import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDesigns } from 'app/shared/model/designs.model';
import { DesignsService } from './designs.service';

@Component({
  selector: 'jhi-designs-delete-dialog',
  templateUrl: './designs-delete-dialog.component.html'
})
export class DesignsDeleteDialogComponent {
  designs: IDesigns;

  constructor(protected designsService: DesignsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.designsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'designsListModification',
        content: 'Deleted an designs'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-designs-delete-popup',
  template: ''
})
export class DesignsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ designs }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DesignsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.designs = designs;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/designs', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/designs', { outlets: { popup: null } }]);
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
