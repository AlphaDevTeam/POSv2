import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISupplierAccount } from 'app/shared/model/supplier-account.model';
import { SupplierAccountService } from './supplier-account.service';

@Component({
  selector: 'jhi-supplier-account-delete-dialog',
  templateUrl: './supplier-account-delete-dialog.component.html'
})
export class SupplierAccountDeleteDialogComponent {
  supplierAccount: ISupplierAccount;

  constructor(
    protected supplierAccountService: SupplierAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.supplierAccountService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'supplierAccountListModification',
        content: 'Deleted an supplierAccount'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-supplier-account-delete-popup',
  template: ''
})
export class SupplierAccountDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ supplierAccount }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SupplierAccountDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.supplierAccount = supplierAccount;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/supplier-account', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/supplier-account', { outlets: { popup: null } }]);
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
