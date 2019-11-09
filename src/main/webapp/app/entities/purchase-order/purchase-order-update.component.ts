import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IPurchaseOrder, PurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from './purchase-order.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from 'app/entities/goods-receipt/goods-receipt.service';

@Component({
  selector: 'jhi-purchase-order-update',
  templateUrl: './purchase-order-update.component.html'
})
export class PurchaseOrderUpdateComponent implements OnInit {
  isSaving: boolean;

  suppliers: ISupplier[];

  locations: ILocation[];

  goodsreceipts: IGoodsReceipt[];
  poDateDp: any;

  editForm = this.fb.group({
    id: [],
    poNumber: [null, [Validators.required]],
    poDate: [null, [Validators.required]],
    supplier: [],
    location: [],
    relatedGRN: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected purchaseOrderService: PurchaseOrderService,
    protected supplierService: SupplierService,
    protected locationService: LocationService,
    protected goodsReceiptService: GoodsReceiptService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
      this.updateForm(purchaseOrder);
    });
    this.supplierService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISupplier[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISupplier[]>) => response.body)
      )
      .subscribe((res: ISupplier[]) => (this.suppliers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.goodsReceiptService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IGoodsReceipt[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGoodsReceipt[]>) => response.body)
      )
      .subscribe((res: IGoodsReceipt[]) => (this.goodsreceipts = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(purchaseOrder: IPurchaseOrder) {
    this.editForm.patchValue({
      id: purchaseOrder.id,
      poNumber: purchaseOrder.poNumber,
      poDate: purchaseOrder.poDate,
      supplier: purchaseOrder.supplier,
      location: purchaseOrder.location,
      relatedGRN: purchaseOrder.relatedGRN
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const purchaseOrder = this.createFromForm();
    if (purchaseOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderService.update(purchaseOrder));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderService.create(purchaseOrder));
    }
  }

  private createFromForm(): IPurchaseOrder {
    return {
      ...new PurchaseOrder(),
      id: this.editForm.get(['id']).value,
      poNumber: this.editForm.get(['poNumber']).value,
      poDate: this.editForm.get(['poDate']).value,
      supplier: this.editForm.get(['supplier']).value,
      location: this.editForm.get(['location']).value,
      relatedGRN: this.editForm.get(['relatedGRN']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrder>>) {
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

  trackSupplierById(index: number, item: ISupplier) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackGoodsReceiptById(index: number, item: IGoodsReceipt) {
    return item.id;
  }
}
