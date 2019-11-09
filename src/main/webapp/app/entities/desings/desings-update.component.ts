import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDesings, Desings } from 'app/shared/model/desings.model';
import { DesingsService } from './desings.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-desings-update',
  templateUrl: './desings-update.component.html'
})
export class DesingsUpdateComponent implements OnInit {
  isSaving: boolean;

  products: IProducts[];

  locations: ILocation[];

  editForm = this.fb.group({
    id: [],
    desingCode: [null, [Validators.required]],
    desingName: [null, [Validators.required]],
    desingPrefix: [null, [Validators.required]],
    desingProfMargin: [null, [Validators.required]],
    relatedProduct: [],
    location: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected desingsService: DesingsService,
    protected productsService: ProductsService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ desings }) => {
      this.updateForm(desings);
    });
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

  updateForm(desings: IDesings) {
    this.editForm.patchValue({
      id: desings.id,
      desingCode: desings.desingCode,
      desingName: desings.desingName,
      desingPrefix: desings.desingPrefix,
      desingProfMargin: desings.desingProfMargin,
      relatedProduct: desings.relatedProduct,
      location: desings.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const desings = this.createFromForm();
    if (desings.id !== undefined) {
      this.subscribeToSaveResponse(this.desingsService.update(desings));
    } else {
      this.subscribeToSaveResponse(this.desingsService.create(desings));
    }
  }

  private createFromForm(): IDesings {
    return {
      ...new Desings(),
      id: this.editForm.get(['id']).value,
      desingCode: this.editForm.get(['desingCode']).value,
      desingName: this.editForm.get(['desingName']).value,
      desingPrefix: this.editForm.get(['desingPrefix']).value,
      desingProfMargin: this.editForm.get(['desingProfMargin']).value,
      relatedProduct: this.editForm.get(['relatedProduct']).value,
      location: this.editForm.get(['location']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDesings>>) {
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

  trackProductsById(index: number, item: IProducts) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }
}
