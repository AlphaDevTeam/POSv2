import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExUser } from 'app/shared/model/ex-user.model';

@Component({
  selector: 'jhi-ex-user-detail',
  templateUrl: './ex-user-detail.component.html'
})
export class ExUserDetailComponent implements OnInit {
  exUser: IExUser;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.exUser = exUser;
    });
  }

  previousState() {
    window.history.back();
  }
}
