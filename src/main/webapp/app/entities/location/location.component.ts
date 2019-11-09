import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ILocation } from 'app/shared/model/location.model';
import { AccountService } from 'app/core/auth/account.service';
import { LocationService } from './location.service';

@Component({
  selector: 'jhi-location',
  templateUrl: './location.component.html'
})
export class LocationComponent implements OnInit, OnDestroy {
  locations: ILocation[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected locationService: LocationService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.locationService
      .query()
      .pipe(
        filter((res: HttpResponse<ILocation[]>) => res.ok),
        map((res: HttpResponse<ILocation[]>) => res.body)
      )
      .subscribe((res: ILocation[]) => {
        this.locations = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInLocations();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ILocation) {
    return item.id;
  }

  registerChangeInLocations() {
    this.eventSubscriber = this.eventManager.subscribe('locationListModification', response => this.loadAll());
  }
}
