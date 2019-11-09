import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseAccount } from 'app/shared/model/purchase-account.model';
import { PurchaseAccountService } from './purchase-account.service';

@Component({
  selector: 'jhi-purchase-account-delete-dialog',
  templateUrl: './purchase-account-delete-dialog.component.html'
})
export class PurchaseAccountDeleteDialogComponent {
  purchaseAccount: IPurchaseAccount;

  constructor(
    protected purchaseAccountService: PurchaseAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.purchaseAccountService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'purchaseAccountListModification',
        content: 'Deleted an purchaseAccount'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-purchase-account-delete-popup',
  template: ''
})
export class PurchaseAccountDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ purchaseAccount }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PurchaseAccountDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.purchaseAccount = purchaseAccount;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/purchase-account', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/purchase-account', { outlets: { popup: null } }]);
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
