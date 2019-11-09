import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';

@Component({
  selector: 'jhi-ex-user-delete-dialog',
  templateUrl: './ex-user-delete-dialog.component.html'
})
export class ExUserDeleteDialogComponent {
  exUser: IExUser;

  constructor(protected exUserService: ExUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.exUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'exUserListModification',
        content: 'Deleted an exUser'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ex-user-delete-popup',
  template: ''
})
export class ExUserDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ exUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ExUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.exUser = exUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/ex-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/ex-user', { outlets: { popup: null } }]);
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
