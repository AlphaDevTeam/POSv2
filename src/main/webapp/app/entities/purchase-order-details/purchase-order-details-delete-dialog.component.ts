import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';
import { PurchaseOrderDetailsService } from './purchase-order-details.service';

@Component({
  selector: 'jhi-purchase-order-details-delete-dialog',
  templateUrl: './purchase-order-details-delete-dialog.component.html'
})
export class PurchaseOrderDetailsDeleteDialogComponent {
  purchaseOrderDetails: IPurchaseOrderDetails;

  constructor(
    protected purchaseOrderDetailsService: PurchaseOrderDetailsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.purchaseOrderDetailsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'purchaseOrderDetailsListModification',
        content: 'Deleted an purchaseOrderDetails'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-purchase-order-details-delete-popup',
  template: ''
})
export class PurchaseOrderDetailsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ purchaseOrderDetails }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PurchaseOrderDetailsDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.purchaseOrderDetails = purchaseOrderDetails;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/purchase-order-details', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/purchase-order-details', { outlets: { popup: null } }]);
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
