import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';
import { PurchaseAccountBalanceService } from './purchase-account-balance.service';

@Component({
  selector: 'jhi-purchase-account-balance-delete-dialog',
  templateUrl: './purchase-account-balance-delete-dialog.component.html'
})
export class PurchaseAccountBalanceDeleteDialogComponent {
  purchaseAccountBalance: IPurchaseAccountBalance;

  constructor(
    protected purchaseAccountBalanceService: PurchaseAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.purchaseAccountBalanceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'purchaseAccountBalanceListModification',
        content: 'Deleted an purchaseAccountBalance'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-purchase-account-balance-delete-popup',
  template: ''
})
export class PurchaseAccountBalanceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ purchaseAccountBalance }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PurchaseAccountBalanceDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.purchaseAccountBalance = purchaseAccountBalance;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/purchase-account-balance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/purchase-account-balance', { outlets: { popup: null } }]);
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
