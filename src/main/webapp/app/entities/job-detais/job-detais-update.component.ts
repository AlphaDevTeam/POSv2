import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IJobDetais, JobDetais } from 'app/shared/model/job-detais.model';
import { JobDetaisService } from './job-detais.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job/job.service';

@Component({
  selector: 'jhi-job-detais-update',
  templateUrl: './job-detais-update.component.html'
})
export class JobDetaisUpdateComponent implements OnInit {
  isSaving: boolean;

  items: IItems[];

  jobs: IJob[];

  editForm = this.fb.group({
    id: [],
    jobItemPrice: [null, [Validators.required]],
    jobItemQty: [null, [Validators.required]],
    item: [],
    job: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected jobDetaisService: JobDetaisService,
    protected itemsService: ItemsService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ jobDetais }) => {
      this.updateForm(jobDetais);
    });
    this.itemsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IItems[]>) => mayBeOk.ok),
        map((response: HttpResponse<IItems[]>) => response.body)
      )
      .subscribe((res: IItems[]) => (this.items = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.jobService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IJob[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJob[]>) => response.body)
      )
      .subscribe((res: IJob[]) => (this.jobs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(jobDetais: IJobDetais) {
    this.editForm.patchValue({
      id: jobDetais.id,
      jobItemPrice: jobDetais.jobItemPrice,
      jobItemQty: jobDetais.jobItemQty,
      item: jobDetais.item,
      job: jobDetais.job
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const jobDetais = this.createFromForm();
    if (jobDetais.id !== undefined) {
      this.subscribeToSaveResponse(this.jobDetaisService.update(jobDetais));
    } else {
      this.subscribeToSaveResponse(this.jobDetaisService.create(jobDetais));
    }
  }

  private createFromForm(): IJobDetais {
    return {
      ...new JobDetais(),
      id: this.editForm.get(['id']).value,
      jobItemPrice: this.editForm.get(['jobItemPrice']).value,
      jobItemQty: this.editForm.get(['jobItemQty']).value,
      item: this.editForm.get(['item']).value,
      job: this.editForm.get(['job']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobDetais>>) {
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

  trackItemsById(index: number, item: IItems) {
    return item.id;
  }

  trackJobById(index: number, item: IJob) {
    return item.id;
  }
}
