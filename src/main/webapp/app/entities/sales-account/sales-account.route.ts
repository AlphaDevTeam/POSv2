import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SalesAccount } from 'app/shared/model/sales-account.model';
import { SalesAccountService } from './sales-account.service';
import { SalesAccountComponent } from './sales-account.component';
import { SalesAccountDetailComponent } from './sales-account-detail.component';
import { SalesAccountUpdateComponent } from './sales-account-update.component';
import { SalesAccountDeletePopupComponent } from './sales-account-delete-dialog.component';
import { ISalesAccount } from 'app/shared/model/sales-account.model';

@Injectable({ providedIn: 'root' })
export class SalesAccountResolve implements Resolve<ISalesAccount> {
  constructor(private service: SalesAccountService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISalesAccount> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SalesAccount>) => response.ok),
        map((salesAccount: HttpResponse<SalesAccount>) => salesAccount.body)
      );
    }
    return of(new SalesAccount());
  }
}

export const salesAccountRoute: Routes = [
  {
    path: '',
    component: SalesAccountComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SalesAccountDetailComponent,
    resolve: {
      salesAccount: SalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SalesAccountUpdateComponent,
    resolve: {
      salesAccount: SalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SalesAccountUpdateComponent,
    resolve: {
      salesAccount: SalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const salesAccountPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SalesAccountDeletePopupComponent,
    resolve: {
      salesAccount: SalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
