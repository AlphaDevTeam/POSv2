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
import { IJob, Job } from 'app/shared/model/job.model';
import { JobService } from './job.service';
import { IJobStatus } from 'app/shared/model/job-status.model';
import { JobStatusService } from 'app/entities/job-status/job-status.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html'
})
export class JobUpdateComponent implements OnInit {
  isSaving: boolean;

  statuses: IJobStatus[];

  locations: ILocation[];

  customers: ICustomer[];
  jobStartDateDp: any;
  jobEndDateDp: any;

  editForm = this.fb.group({
    id: [],
    jobCode: [null, [Validators.required]],
    jobDescription: [],
    jobStartDate: [],
    jobEndDate: [],
    jobAmount: [],
    status: [],
    location: [],
    customer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected jobService: JobService,
    protected jobStatusService: JobStatusService,
    protected locationService: LocationService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);
    });
    this.jobStatusService
      .query({ filter: 'job-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IJobStatus[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJobStatus[]>) => response.body)
      )
      .subscribe(
        (res: IJobStatus[]) => {
          if (!this.editForm.get('status').value || !this.editForm.get('status').value.id) {
            this.statuses = res;
          } else {
            this.jobStatusService
              .find(this.editForm.get('status').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IJobStatus>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IJobStatus>) => subResponse.body)
              )
              .subscribe(
                (subRes: IJobStatus) => (this.statuses = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.customerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICustomer[]>) => response.body)
      )
      .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(job: IJob) {
    this.editForm.patchValue({
      id: job.id,
      jobCode: job.jobCode,
      jobDescription: job.jobDescription,
      jobStartDate: job.jobStartDate,
      jobEndDate: job.jobEndDate,
      jobAmount: job.jobAmount,
      status: job.status,
      location: job.location,
      customer: job.customer
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  private createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id']).value,
      jobCode: this.editForm.get(['jobCode']).value,
      jobDescription: this.editForm.get(['jobDescription']).value,
      jobStartDate: this.editForm.get(['jobStartDate']).value,
      jobEndDate: this.editForm.get(['jobEndDate']).value,
      jobAmount: this.editForm.get(['jobAmount']).value,
      status: this.editForm.get(['status']).value,
      location: this.editForm.get(['location']).value,
      customer: this.editForm.get(['customer']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>) {
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

  trackJobStatusById(index: number, item: IJobStatus) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackCustomerById(index: number, item: ICustomer) {
    return item.id;
  }
}
