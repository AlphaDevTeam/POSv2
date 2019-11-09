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
import { ISalesAccount, SalesAccount } from 'app/shared/model/sales-account.model';
import { SalesAccountService } from './sales-account.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

@Component({
  selector: 'jhi-sales-account-update',
  templateUrl: './sales-account-update.component.html'
})
export class SalesAccountUpdateComponent implements OnInit {
  isSaving: boolean;

  locations: ILocation[];

  transactiontypes: ITransactionType[];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmountDR: [null, [Validators.required]],
    transactionAmountCR: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    location: [],
    transactionType: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected salesAccountService: SalesAccountService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ salesAccount }) => {
      this.updateForm(salesAccount);
    });
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.transactionTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITransactionType[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITransactionType[]>) => response.body)
      )
      .subscribe((res: ITransactionType[]) => (this.transactiontypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(salesAccount: ISalesAccount) {
    this.editForm.patchValue({
      id: salesAccount.id,
      transactionDate: salesAccount.transactionDate,
      transactionDescription: salesAccount.transactionDescription,
      transactionAmountDR: salesAccount.transactionAmountDR,
      transactionAmountCR: salesAccount.transactionAmountCR,
      transactionBalance: salesAccount.transactionBalance,
      location: salesAccount.location,
      transactionType: salesAccount.transactionType
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const salesAccount = this.createFromForm();
    if (salesAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.salesAccountService.update(salesAccount));
    } else {
      this.subscribeToSaveResponse(this.salesAccountService.create(salesAccount));
    }
  }

  private createFromForm(): ISalesAccount {
    return {
      ...new SalesAccount(),
      id: this.editForm.get(['id']).value,
      transactionDate: this.editForm.get(['transactionDate']).value,
      transactionDescription: this.editForm.get(['transactionDescription']).value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR']).value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR']).value,
      transactionBalance: this.editForm.get(['transactionBalance']).value,
      location: this.editForm.get(['location']).value,
      transactionType: this.editForm.get(['transactionType']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalesAccount>>) {
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

  trackTransactionTypeById(index: number, item: ITransactionType) {
    return item.id;
  }
}
