import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';
import { CustomerAccountBalanceService } from './customer-account-balance.service';
import { CustomerAccountBalanceComponent } from './customer-account-balance.component';
import { CustomerAccountBalanceDetailComponent } from './customer-account-balance-detail.component';
import { CustomerAccountBalanceUpdateComponent } from './customer-account-balance-update.component';
import { CustomerAccountBalanceDeletePopupComponent } from './customer-account-balance-delete-dialog.component';
import { ICustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';

@Injectable({ providedIn: 'root' })
export class CustomerAccountBalanceResolve implements Resolve<ICustomerAccountBalance> {
  constructor(private service: CustomerAccountBalanceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomerAccountBalance> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CustomerAccountBalance>) => response.ok),
        map((customerAccountBalance: HttpResponse<CustomerAccountBalance>) => customerAccountBalance.body)
      );
    }
    return of(new CustomerAccountBalance());
  }
}

export const customerAccountBalanceRoute: Routes = [
  {
    path: '',
    component: CustomerAccountBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CustomerAccountBalanceDetailComponent,
    resolve: {
      customerAccountBalance: CustomerAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CustomerAccountBalanceUpdateComponent,
    resolve: {
      customerAccountBalance: CustomerAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CustomerAccountBalanceUpdateComponent,
    resolve: {
      customerAccountBalance: CustomerAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const customerAccountBalancePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CustomerAccountBalanceDeletePopupComponent,
    resolve: {
      customerAccountBalance: CustomerAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
