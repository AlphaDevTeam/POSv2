import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICashBookBalance } from 'app/shared/model/cash-book-balance.model';
import { CashBookBalanceService } from './cash-book-balance.service';

@Component({
  selector: 'jhi-cash-book-balance-delete-dialog',
  templateUrl: './cash-book-balance-delete-dialog.component.html'
})
export class CashBookBalanceDeleteDialogComponent {
  cashBookBalance: ICashBookBalance;

  constructor(
    protected cashBookBalanceService: CashBookBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.cashBookBalanceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'cashBookBalanceListModification',
        content: 'Deleted an cashBookBalance'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-cash-book-balance-delete-popup',
  template: ''
})
export class CashBookBalanceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cashBookBalance }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CashBookBalanceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.cashBookBalance = cashBookBalance;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/cash-book-balance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/cash-book-balance', { outlets: { popup: null } }]);
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
