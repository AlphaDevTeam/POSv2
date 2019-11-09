import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';
import { SupplierAccountBalanceService } from './supplier-account-balance.service';

@Component({
  selector: 'jhi-supplier-account-balance-delete-dialog',
  templateUrl: './supplier-account-balance-delete-dialog.component.html'
})
export class SupplierAccountBalanceDeleteDialogComponent {
  supplierAccountBalance: ISupplierAccountBalance;

  constructor(
    protected supplierAccountBalanceService: SupplierAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.supplierAccountBalanceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'supplierAccountBalanceListModification',
        content: 'Deleted an supplierAccountBalance'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-supplier-account-balance-delete-popup',
  template: ''
})
export class SupplierAccountBalanceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ supplierAccountBalance }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SupplierAccountBalanceDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.supplierAccountBalance = supplierAccountBalance;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/supplier-account-balance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/supplier-account-balance', { outlets: { popup: null } }]);
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
