import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IExUser, ExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company/company.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-ex-user-update',
  templateUrl: './ex-user-update.component.html'
})
export class ExUserUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  companies: ICompany[];

  locations: ILocation[];

  editForm = this.fb.group({
    id: [],
    userKey: [],
    relatedUser: [],
    company: [],
    locations: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected exUserService: ExUserService,
    protected userService: UserService,
    protected companyService: CompanyService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.updateForm(exUser);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.companyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe((res: ICompany[]) => (this.companies = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateLocation(companyId: number) {
    this.locationService
      .query({ 'companyId.equals': companyId })
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(exUser: IExUser) {
    this.editForm.patchValue({
      id: exUser.id,
      userKey: exUser.userKey,
      relatedUser: exUser.relatedUser,
      company: exUser.company,
      locations: exUser.locations
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const exUser = this.createFromForm();
    if (exUser.id !== undefined) {
      this.subscribeToSaveResponse(this.exUserService.update(exUser));
    } else {
      this.subscribeToSaveResponse(this.exUserService.create(exUser));
    }
  }

  private createFromForm(): IExUser {
    return {
      ...new ExUser(),
      id: this.editForm.get(['id']).value,
      userKey: this.editForm.get(['userKey']).value,
      relatedUser: this.editForm.get(['relatedUser']).value,
      company: this.editForm.get(['company']).value,
      locations: this.editForm.get(['locations']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExUser>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
