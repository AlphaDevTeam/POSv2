import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerAccount } from 'app/shared/model/customer-account.model';
import { CustomerAccountService } from './customer-account.service';

@Component({
  selector: 'jhi-customer-account-delete-dialog',
  templateUrl: './customer-account-delete-dialog.component.html'
})
export class CustomerAccountDeleteDialogComponent {
  customerAccount: ICustomerAccount;

  constructor(
    protected customerAccountService: CustomerAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.customerAccountService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'customerAccountListModification',
        content: 'Deleted an customerAccount'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-customer-account-delete-popup',
  template: ''
})
export class CustomerAccountDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customerAccount }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CustomerAccountDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.customerAccount = customerAccount;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/customer-account', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/customer-account', { outlets: { popup: null } }]);
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
