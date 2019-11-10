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
import { IItems, Items } from 'app/shared/model/items.model';
import { ItemsService } from './items.service';
import { IDesings } from 'app/shared/model/desings.model';
import { DesingsService } from 'app/entities/desings/desings.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-items-update',
  templateUrl: './items-update.component.html'
})
export class ItemsUpdateComponent implements OnInit {
  isSaving: boolean;

  desings: IDesings[];

  products: IProducts[];

  locations: ILocation[];
  originalStockDateDp: any;
  modifiedStockDateDp: any;

  editForm = this.fb.group({
    id: [],
    itemCode: [null, [Validators.required]],
    itemName: [null, [Validators.required]],
    itemDescription: [null, [Validators.required]],
    itemPrice: [null, [Validators.required]],
    itemSerial: [null, [Validators.required]],
    itemSupplierSerial: [],
    itemCost: [null, [Validators.required]],
    itemSalePrice: [],
    originalStockDate: [null, [Validators.required]],
    modifiedStockDate: [],
    relatedDesign: [],
    relatedProduct: [],
    location: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected itemsService: ItemsService,
    protected desingsService: DesingsService,
    protected productsService: ProductsService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ items }) => {
      this.updateForm(items);
    });
    this.desingsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDesings[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDesings[]>) => response.body)
      )
      .subscribe((res: IDesings[]) => (this.desings = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.productsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProducts[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProducts[]>) => response.body)
      )
      .subscribe((res: IProducts[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateProduct(productID: number) {
    this.desingsService
      .query({ 'relatedProductId.equals': productID })
      .pipe(
        filter((mayBeOk: HttpResponse<IDesings[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDesings[]>) => response.body)
      )
      .subscribe((res: IDesings[]) => (this.desings = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateLocation(locationID: number) {
    this.productsService
      .query({ 'locationId.equals': locationID })
      .pipe(
        filter((mayBeOk: HttpResponse<IProducts[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProducts[]>) => response.body)
      )
      .subscribe((res: IProducts[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(items: IItems) {
    this.editForm.patchValue({
      id: items.id,
      itemCode: items.itemCode,
      itemName: items.itemName,
      itemDescription: items.itemDescription,
      itemPrice: items.itemPrice,
      itemSerial: items.itemSerial,
      itemSupplierSerial: items.itemSupplierSerial,
      itemCost: items.itemCost,
      itemSalePrice: items.itemSalePrice,
      originalStockDate: items.originalStockDate,
      modifiedStockDate: items.modifiedStockDate,
      relatedDesign: items.relatedDesign,
      relatedProduct: items.relatedProduct,
      location: items.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const items = this.createFromForm();
    if (items.id !== undefined) {
      this.subscribeToSaveResponse(this.itemsService.update(items));
    } else {
      this.subscribeToSaveResponse(this.itemsService.create(items));
    }
  }

  private createFromForm(): IItems {
    return {
      ...new Items(),
      id: this.editForm.get(['id']).value,
      itemCode: this.editForm.get(['itemCode']).value,
      itemName: this.editForm.get(['itemName']).value,
      itemDescription: this.editForm.get(['itemDescription']).value,
      itemPrice: this.editForm.get(['itemPrice']).value,
      itemSerial: this.editForm.get(['itemSerial']).value,
      itemSupplierSerial: this.editForm.get(['itemSupplierSerial']).value,
      itemCost: this.editForm.get(['itemCost']).value,
      itemSalePrice: this.editForm.get(['itemSalePrice']).value,
      originalStockDate: this.editForm.get(['originalStockDate']).value,
      modifiedStockDate: this.editForm.get(['modifiedStockDate']).value,
      relatedDesign: this.editForm.get(['relatedDesign']).value,
      relatedProduct: this.editForm.get(['relatedProduct']).value,
      location: this.editForm.get(['location']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItems>>) {
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

  trackDesingsById(index: number, item: IDesings) {
    return item.id;
  }

  trackProductsById(index: number, item: IProducts) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }
}
