import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICashBook } from 'app/shared/model/cash-book.model';
import { CashBookService } from './cash-book.service';

@Component({
  selector: 'jhi-cash-book-delete-dialog',
  templateUrl: './cash-book-delete-dialog.component.html'
})
export class CashBookDeleteDialogComponent {
  cashBook: ICashBook;

  constructor(protected cashBookService: CashBookService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.cashBookService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'cashBookListModification',
        content: 'Deleted an cashBook'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-cash-book-delete-popup',
  template: ''
})
export class CashBookDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cashBook }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CashBookDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.cashBook = cashBook;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/cash-book', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/cash-book', { outlets: { popup: null } }]);
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
