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
import { IItemBinCard, ItemBinCard } from 'app/shared/model/item-bin-card.model';
import { ItemBinCardService } from './item-bin-card.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';

@Component({
  selector: 'jhi-item-bin-card-update',
  templateUrl: './item-bin-card-update.component.html'
})
export class ItemBinCardUpdateComponent implements OnInit {
  isSaving: boolean;

  locations: ILocation[];

  items: IItems[];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionQty: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    location: [],
    item: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected itemBinCardService: ItemBinCardService,
    protected locationService: LocationService,
    protected itemsService: ItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ itemBinCard }) => {
      this.updateForm(itemBinCard);
    });
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.itemsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IItems[]>) => mayBeOk.ok),
        map((response: HttpResponse<IItems[]>) => response.body)
      )
      .subscribe((res: IItems[]) => (this.items = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(itemBinCard: IItemBinCard) {
    this.editForm.patchValue({
      id: itemBinCard.id,
      transactionDate: itemBinCard.transactionDate,
      transactionDescription: itemBinCard.transactionDescription,
      transactionQty: itemBinCard.transactionQty,
      transactionBalance: itemBinCard.transactionBalance,
      location: itemBinCard.location,
      item: itemBinCard.item
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const itemBinCard = this.createFromForm();
    if (itemBinCard.id !== undefined) {
      this.subscribeToSaveResponse(this.itemBinCardService.update(itemBinCard));
    } else {
      this.subscribeToSaveResponse(this.itemBinCardService.create(itemBinCard));
    }
  }

  private createFromForm(): IItemBinCard {
    return {
      ...new ItemBinCard(),
      id: this.editForm.get(['id']).value,
      transactionDate: this.editForm.get(['transactionDate']).value,
      transactionDescription: this.editForm.get(['transactionDescription']).value,
      transactionQty: this.editForm.get(['transactionQty']).value,
      transactionBalance: this.editForm.get(['transactionBalance']).value,
      location: this.editForm.get(['location']).value,
      item: this.editForm.get(['item']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemBinCard>>) {
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

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackItemsById(index: number, item: IItems) {
    return item.id;
  }
}
