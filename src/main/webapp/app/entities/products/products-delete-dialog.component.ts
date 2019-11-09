import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from './products.service';

@Component({
  selector: 'jhi-products-delete-dialog',
  templateUrl: './products-delete-dialog.component.html'
})
export class ProductsDeleteDialogComponent {
  products: IProducts;

  constructor(protected productsService: ProductsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.productsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'productsListModification',
        content: 'Deleted an products'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-products-delete-popup',
  template: ''
})
export class ProductsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ products }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProductsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.products = products;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/products', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/products', { outlets: { popup: null } }]);
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
