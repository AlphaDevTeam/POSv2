import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDesigns, Designs } from 'app/shared/model/designs.model';
import { DesignsService } from './designs.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-designs-update',
  templateUrl: './designs-update.component.html'
})
export class DesignsUpdateComponent implements OnInit {
  isSaving: boolean;

  products: IProducts[];

  locations: ILocation[];

  editForm = this.fb.group({
    id: [],
    designCode: [null, [Validators.required]],
    designName: [null, [Validators.required]],
    designPrefix: [null, [Validators.required]],
    designProfMargin: [null, [Validators.required]],
    relatedProduct: [],
    location: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected designsService: DesignsService,
    protected productsService: ProductsService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ designs }) => {
      this.updateForm(designs);
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

  updateForm(designs: IDesigns) {
    this.editForm.patchValue({
      id: designs.id,
      designCode: designs.designCode,
      designName: designs.designName,
      designPrefix: designs.designPrefix,
      designProfMargin: designs.designProfMargin,
      relatedProduct: designs.relatedProduct,
      location: designs.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const designs = this.createFromForm();
    if (designs.id !== undefined) {
      this.subscribeToSaveResponse(this.designsService.update(designs));
    } else {
      this.subscribeToSaveResponse(this.designsService.create(designs));
    }
  }

  private createFromForm(): IDesigns {
    return {
      ...new Designs(),
      id: this.editForm.get(['id']).value,
      designCode: this.editForm.get(['designCode']).value,
      designName: this.editForm.get(['designName']).value,
      designPrefix: this.editForm.get(['designPrefix']).value,
      designProfMargin: this.editForm.get(['designProfMargin']).value,
      relatedProduct: this.editForm.get(['relatedProduct']).value,
      location: this.editForm.get(['location']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDesigns>>) {
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
