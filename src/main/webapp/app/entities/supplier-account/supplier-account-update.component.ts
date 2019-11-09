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
import { ISupplierAccount, SupplierAccount } from 'app/shared/model/supplier-account.model';
import { SupplierAccountService } from './supplier-account.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';

@Component({
  selector: 'jhi-supplier-account-update',
  templateUrl: './supplier-account-update.component.html'
})
export class SupplierAccountUpdateComponent implements OnInit {
  isSaving: boolean;

  locations: ILocation[];

  transactiontypes: ITransactionType[];

  suppliers: ISupplier[];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmountDR: [null, [Validators.required]],
    transactionAmountCR: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    location: [],
    transactionType: [],
    supplier: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected supplierAccountService: SupplierAccountService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected supplierService: SupplierService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ supplierAccount }) => {
      this.updateForm(supplierAccount);
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
    this.supplierService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISupplier[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISupplier[]>) => response.body)
      )
      .subscribe((res: ISupplier[]) => (this.suppliers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(supplierAccount: ISupplierAccount) {
    this.editForm.patchValue({
      id: supplierAccount.id,
      transactionDate: supplierAccount.transactionDate,
      transactionDescription: supplierAccount.transactionDescription,
      transactionAmountDR: supplierAccount.transactionAmountDR,
      transactionAmountCR: supplierAccount.transactionAmountCR,
      transactionBalance: supplierAccount.transactionBalance,
      location: supplierAccount.location,
      transactionType: supplierAccount.transactionType,
      supplier: supplierAccount.supplier
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const supplierAccount = this.createFromForm();
    if (supplierAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.supplierAccountService.update(supplierAccount));
    } else {
      this.subscribeToSaveResponse(this.supplierAccountService.create(supplierAccount));
    }
  }

  private createFromForm(): ISupplierAccount {
    return {
      ...new SupplierAccount(),
      id: this.editForm.get(['id']).value,
      transactionDate: this.editForm.get(['transactionDate']).value,
      transactionDescription: this.editForm.get(['transactionDescription']).value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR']).value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR']).value,
      transactionBalance: this.editForm.get(['transactionBalance']).value,
      location: this.editForm.get(['location']).value,
      transactionType: this.editForm.get(['transactionType']).value,
      supplier: this.editForm.get(['supplier']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplierAccount>>) {
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

  trackSupplierById(index: number, item: ISupplier) {
    return item.id;
  }
}
