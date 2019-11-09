import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';
import { CustomerAccountBalanceService } from './customer-account-balance.service';

@Component({
  selector: 'jhi-customer-account-balance-delete-dialog',
  templateUrl: './customer-account-balance-delete-dialog.component.html'
})
export class CustomerAccountBalanceDeleteDialogComponent {
  customerAccountBalance: ICustomerAccountBalance;

  constructor(
    protected customerAccountBalanceService: CustomerAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.customerAccountBalanceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'customerAccountBalanceListModification',
        content: 'Deleted an customerAccountBalance'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-customer-account-balance-delete-popup',
  template: ''
})
export class CustomerAccountBalanceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customerAccountBalance }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CustomerAccountBalanceDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.customerAccountBalance = customerAccountBalance;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/customer-account-balance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/customer-account-balance', { outlets: { popup: null } }]);
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
