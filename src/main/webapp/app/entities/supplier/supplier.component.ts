import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ISupplier } from 'app/shared/model/supplier.model';
import { AccountService } from 'app/core/auth/account.service';
import { SupplierService } from './supplier.service';

@Component({
  selector: 'jhi-supplier',
  templateUrl: './supplier.component.html'
})
export class SupplierComponent implements OnInit, OnDestroy {
  suppliers: ISupplier[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected supplierService: SupplierService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.supplierService
      .query()
      .pipe(
        filter((res: HttpResponse<ISupplier[]>) => res.ok),
        map((res: HttpResponse<ISupplier[]>) => res.body)
      )
      .subscribe((res: ISupplier[]) => {
        this.suppliers = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSuppliers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISupplier) {
    return item.id;
  }

  registerChangeInSuppliers() {
    this.eventSubscriber = this.eventManager.subscribe('supplierListModification', response => this.loadAll());
  }
}
