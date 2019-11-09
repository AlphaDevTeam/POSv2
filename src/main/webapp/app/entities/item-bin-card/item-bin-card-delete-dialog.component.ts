import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IItemBinCard } from 'app/shared/model/item-bin-card.model';
import { ItemBinCardService } from './item-bin-card.service';

@Component({
  selector: 'jhi-item-bin-card-delete-dialog',
  templateUrl: './item-bin-card-delete-dialog.component.html'
})
export class ItemBinCardDeleteDialogComponent {
  itemBinCard: IItemBinCard;

  constructor(
    protected itemBinCardService: ItemBinCardService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.itemBinCardService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'itemBinCardListModification',
        content: 'Deleted an itemBinCard'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-item-bin-card-delete-popup',
  template: ''
})
export class ItemBinCardDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ itemBinCard }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ItemBinCardDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.itemBinCard = itemBinCard;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/item-bin-card', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/item-bin-card', { outlets: { popup: null } }]);
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
