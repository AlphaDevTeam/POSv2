import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesings } from 'app/shared/model/desings.model';

@Component({
  selector: 'jhi-desings-detail',
  templateUrl: './desings-detail.component.html'
})
export class DesingsDetailComponent implements OnInit {
  desings: IDesings;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ desings }) => {
      this.desings = desings;
    });
  }

  previousState() {
    window.history.back();
  }
}
