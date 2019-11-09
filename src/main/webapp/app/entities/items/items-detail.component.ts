import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItems } from 'app/shared/model/items.model';

@Component({
  selector: 'jhi-items-detail',
  templateUrl: './items-detail.component.html'
})
export class ItemsDetailComponent implements OnInit {
  items: IItems;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ items }) => {
      this.items = items;
    });
  }

  previousState() {
    window.history.back();
  }
}
