import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';
import { SupplierAccountBalanceService } from './supplier-account-balance.service';
import { SupplierAccountBalanceComponent } from './supplier-account-balance.component';
import { SupplierAccountBalanceDetailComponent } from './supplier-account-balance-detail.component';
import { SupplierAccountBalanceUpdateComponent } from './supplier-account-balance-update.component';
import { SupplierAccountBalanceDeletePopupComponent } from './supplier-account-balance-delete-dialog.component';
import { ISupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';

@Injectable({ providedIn: 'root' })
export class SupplierAccountBalanceResolve implements Resolve<ISupplierAccountBalance> {
  constructor(private service: SupplierAccountBalanceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISupplierAccountBalance> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SupplierAccountBalance>) => response.ok),
        map((supplierAccountBalance: HttpResponse<SupplierAccountBalance>) => supplierAccountBalance.body)
      );
    }
    return of(new SupplierAccountBalance());
  }
}

export const supplierAccountBalanceRoute: Routes = [
  {
    path: '',
    component: SupplierAccountBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SupplierAccountBalanceDetailComponent,
    resolve: {
      supplierAccountBalance: SupplierAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SupplierAccountBalanceUpdateComponent,
    resolve: {
      supplierAccountBalance: SupplierAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SupplierAccountBalanceUpdateComponent,
    resolve: {
      supplierAccountBalance: SupplierAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const supplierAccountBalancePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SupplierAccountBalanceDeletePopupComponent,
    resolve: {
      supplierAccountBalance: SupplierAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
