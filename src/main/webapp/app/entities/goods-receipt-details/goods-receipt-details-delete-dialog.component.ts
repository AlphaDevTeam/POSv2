import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';

@Component({
  selector: 'jhi-goods-receipt-details-delete-dialog',
  templateUrl: './goods-receipt-details-delete-dialog.component.html'
})
export class GoodsReceiptDetailsDeleteDialogComponent {
  goodsReceiptDetails: IGoodsReceiptDetails;

  constructor(
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.goodsReceiptDetailsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'goodsReceiptDetailsListModification',
        content: 'Deleted an goodsReceiptDetails'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-goods-receipt-details-delete-popup',
  template: ''
})
export class GoodsReceiptDetailsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ goodsReceiptDetails }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GoodsReceiptDetailsDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.goodsReceiptDetails = goodsReceiptDetails;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/goods-receipt-details', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/goods-receipt-details', { outlets: { popup: null } }]);
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
