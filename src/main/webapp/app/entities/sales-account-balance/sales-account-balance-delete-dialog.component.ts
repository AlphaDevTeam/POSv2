import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISalesAccountBalance } from 'app/shared/model/sales-account-balance.model';
import { SalesAccountBalanceService } from './sales-account-balance.service';

@Component({
  selector: 'jhi-sales-account-balance-delete-dialog',
  templateUrl: './sales-account-balance-delete-dialog.component.html'
})
export class SalesAccountBalanceDeleteDialogComponent {
  salesAccountBalance: ISalesAccountBalance;

  constructor(
    protected salesAccountBalanceService: SalesAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.salesAccountBalanceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'salesAccountBalanceListModification',
        content: 'Deleted an salesAccountBalance'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-sales-account-balance-delete-popup',
  template: ''
})
export class SalesAccountBalanceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ salesAccountBalance }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SalesAccountBalanceDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.salesAccountBalance = salesAccountBalance;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/sales-account-balance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/sales-account-balance', { outlets: { popup: null } }]);
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
