import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISalesAccount } from 'app/shared/model/sales-account.model';
import { SalesAccountService } from './sales-account.service';

@Component({
  selector: 'jhi-sales-account-delete-dialog',
  templateUrl: './sales-account-delete-dialog.component.html'
})
export class SalesAccountDeleteDialogComponent {
  salesAccount: ISalesAccount;

  constructor(
    protected salesAccountService: SalesAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.salesAccountService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'salesAccountListModification',
        content: 'Deleted an salesAccount'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-sales-account-delete-popup',
  template: ''
})
export class SalesAccountDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ salesAccount }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SalesAccountDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.salesAccount = salesAccount;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/sales-account', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/sales-account', { outlets: { popup: null } }]);
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
