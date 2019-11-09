import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SalesAccountBalance } from 'app/shared/model/sales-account-balance.model';
import { SalesAccountBalanceService } from './sales-account-balance.service';
import { SalesAccountBalanceComponent } from './sales-account-balance.component';
import { SalesAccountBalanceDetailComponent } from './sales-account-balance-detail.component';
import { SalesAccountBalanceUpdateComponent } from './sales-account-balance-update.component';
import { SalesAccountBalanceDeletePopupComponent } from './sales-account-balance-delete-dialog.component';
import { ISalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

@Injectable({ providedIn: 'root' })
export class SalesAccountBalanceResolve implements Resolve<ISalesAccountBalance> {
  constructor(private service: SalesAccountBalanceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISalesAccountBalance> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SalesAccountBalance>) => response.ok),
        map((salesAccountBalance: HttpResponse<SalesAccountBalance>) => salesAccountBalance.body)
      );
    }
    return of(new SalesAccountBalance());
  }
}

export const salesAccountBalanceRoute: Routes = [
  {
    path: '',
    component: SalesAccountBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SalesAccountBalanceDetailComponent,
    resolve: {
      salesAccountBalance: SalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SalesAccountBalanceUpdateComponent,
    resolve: {
      salesAccountBalance: SalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SalesAccountBalanceUpdateComponent,
    resolve: {
      salesAccountBalance: SalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const salesAccountBalancePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SalesAccountBalanceDeletePopupComponent,
    resolve: {
      salesAccountBalance: SalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
