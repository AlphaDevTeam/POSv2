import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobDetais } from 'app/shared/model/job-detais.model';

@Component({
  selector: 'jhi-job-detais-detail',
  templateUrl: './job-detais-detail.component.html'
})
export class JobDetaisDetailComponent implements OnInit {
  jobDetais: IJobDetais;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ jobDetais }) => {
      this.jobDetais = jobDetais;
    });
  }

  previousState() {
    window.history.back();
  }
}
