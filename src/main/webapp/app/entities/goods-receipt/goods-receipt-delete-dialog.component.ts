import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from './goods-receipt.service';

@Component({
  selector: 'jhi-goods-receipt-delete-dialog',
  templateUrl: './goods-receipt-delete-dialog.component.html'
})
export class GoodsReceiptDeleteDialogComponent {
  goodsReceipt: IGoodsReceipt;

  constructor(
    protected goodsReceiptService: GoodsReceiptService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.goodsReceiptService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'goodsReceiptListModification',
        content: 'Deleted an goodsReceipt'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-goods-receipt-delete-popup',
  template: ''
})
export class GoodsReceiptDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ goodsReceipt }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GoodsReceiptDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.goodsReceipt = goodsReceipt;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/goods-receipt', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/goods-receipt', { outlets: { popup: null } }]);
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
