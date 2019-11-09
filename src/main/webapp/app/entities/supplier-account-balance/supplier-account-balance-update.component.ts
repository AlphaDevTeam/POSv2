import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ISupplierAccountBalance, SupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';
import { SupplierAccountBalanceService } from './supplier-account-balance.service';

@Component({
  selector: 'jhi-supplier-account-balance-update',
  templateUrl: './supplier-account-balance-update.component.html'
})
export class SupplierAccountBalanceUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]]
  });

  constructor(
    protected supplierAccountBalanceService: SupplierAccountBalanceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ supplierAccountBalance }) => {
      this.updateForm(supplierAccountBalance);
    });
  }

  updateForm(supplierAccountBalance: ISupplierAccountBalance) {
    this.editForm.patchValue({
      id: supplierAccountBalance.id,
      balance: supplierAccountBalance.balance
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const supplierAccountBalance = this.createFromForm();
    if (supplierAccountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.supplierAccountBalanceService.update(supplierAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.supplierAccountBalanceService.create(supplierAccountBalance));
    }
  }

  private createFromForm(): ISupplierAccountBalance {
    return {
      ...new SupplierAccountBalance(),
      id: this.editForm.get(['id']).value,
      balance: this.editForm.get(['balance']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplierAccountBalance>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
