import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPurchaseOrderDetails, PurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';
import { PurchaseOrderDetailsService } from './purchase-order-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/purchase-order.service';

@Component({
  selector: 'jhi-purchase-order-details-update',
  templateUrl: './purchase-order-details-update.component.html'
})
export class PurchaseOrderDetailsUpdateComponent implements OnInit {
  isSaving: boolean;

  items: IItems[];

  purchaseorders: IPurchaseOrder[];

  editForm = this.fb.group({
    id: [],
    itemQty: [null, [Validators.required]],
    item: [],
    po: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected purchaseOrderDetailsService: PurchaseOrderDetailsService,
    protected itemsService: ItemsService,
    protected purchaseOrderService: PurchaseOrderService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ purchaseOrderDetails }) => {
      this.updateForm(purchaseOrderDetails);
    });
    this.itemsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IItems[]>) => mayBeOk.ok),
        map((response: HttpResponse<IItems[]>) => response.body)
      )
      .subscribe((res: IItems[]) => (this.items = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.purchaseOrderService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPurchaseOrder[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPurchaseOrder[]>) => response.body)
      )
      .subscribe((res: IPurchaseOrder[]) => (this.purchaseorders = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(purchaseOrderDetails: IPurchaseOrderDetails) {
    this.editForm.patchValue({
      id: purchaseOrderDetails.id,
      itemQty: purchaseOrderDetails.itemQty,
      item: purchaseOrderDetails.item,
      po: purchaseOrderDetails.po
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const purchaseOrderDetails = this.createFromForm();
    if (purchaseOrderDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderDetailsService.update(purchaseOrderDetails));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderDetailsService.create(purchaseOrderDetails));
    }
  }

  private createFromForm(): IPurchaseOrderDetails {
    return {
      ...new PurchaseOrderDetails(),
      id: this.editForm.get(['id']).value,
      itemQty: this.editForm.get(['itemQty']).value,
      item: this.editForm.get(['item']).value,
      po: this.editForm.get(['po']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrderDetails>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackItemsById(index: number, item: IItems) {
    return item.id;
  }

  trackPurchaseOrderById(index: number, item: IPurchaseOrder) {
    return item.id;
  }
}
