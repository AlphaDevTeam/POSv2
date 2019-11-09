import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ICompany } from 'app/shared/model/company.model';
import { AccountService } from 'app/core/auth/account.service';
import { CompanyService } from './company.service';

@Component({
  selector: 'jhi-company',
  templateUrl: './company.component.html'
})
export class CompanyComponent implements OnInit, OnDestroy {
  companies: ICompany[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected companyService: CompanyService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.companyService
      .query()
      .pipe(
        filter((res: HttpResponse<ICompany[]>) => res.ok),
        map((res: HttpResponse<ICompany[]>) => res.body)
      )
      .subscribe((res: ICompany[]) => {
        this.companies = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCompanies();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICompany) {
    return item.id;
  }

  registerChangeInCompanies() {
    this.eventSubscriber = this.eventManager.subscribe('companyListModification', response => this.loadAll());
  }
}
