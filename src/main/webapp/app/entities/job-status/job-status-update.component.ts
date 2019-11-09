import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IJobStatus, JobStatus } from 'app/shared/model/job-status.model';
import { JobStatusService } from './job-status.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-job-status-update',
  templateUrl: './job-status-update.component.html'
})
export class JobStatusUpdateComponent implements OnInit {
  isSaving: boolean;

  locations: ILocation[];

  editForm = this.fb.group({
    id: [],
    jobStatusCode: [null, [Validators.required]],
    jobStatusDescription: [],
    isActive: [],
    location: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected jobStatusService: JobStatusService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ jobStatus }) => {
      this.updateForm(jobStatus);
    });
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(jobStatus: IJobStatus) {
    this.editForm.patchValue({
      id: jobStatus.id,
      jobStatusCode: jobStatus.jobStatusCode,
      jobStatusDescription: jobStatus.jobStatusDescription,
      isActive: jobStatus.isActive,
      location: jobStatus.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const jobStatus = this.createFromForm();
    if (jobStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.jobStatusService.update(jobStatus));
    } else {
      this.subscribeToSaveResponse(this.jobStatusService.create(jobStatus));
    }
  }

  private createFromForm(): IJobStatus {
    return {
      ...new JobStatus(),
      id: this.editForm.get(['id']).value,
      jobStatusCode: this.editForm.get(['jobStatusCode']).value,
      jobStatusDescription: this.editForm.get(['jobStatusDescription']).value,
      isActive: this.editForm.get(['isActive']).value,
      location: this.editForm.get(['location']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobStatus>>) {
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
}
