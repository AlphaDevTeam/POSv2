import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from './items.service';

@Component({
  selector: 'jhi-items-delete-dialog',
  templateUrl: './items-delete-dialog.component.html'
})
export class ItemsDeleteDialogComponent {
  items: IItems;

  constructor(protected itemsService: ItemsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.itemsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'itemsListModification',
        content: 'Deleted an items'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-items-delete-popup',
  template: ''
})
export class ItemsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ items }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ItemsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.items = items;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/items', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/items', { outlets: { popup: null } }]);
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
