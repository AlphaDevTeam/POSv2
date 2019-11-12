import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesigns } from 'app/shared/model/designs.model';

@Component({
  selector: 'jhi-designs-detail',
  templateUrl: './designs-detail.component.html'
})
export class DesignsDetailComponent implements OnInit {
  designs: IDesigns;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ designs }) => {
      this.designs = designs;
    });
  }

  previousState() {
    window.history.back();
  }
}
