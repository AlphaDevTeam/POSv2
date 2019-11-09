import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';
import { PurchaseAccountBalanceService } from './purchase-account-balance.service';
import { PurchaseAccountBalanceComponent } from './purchase-account-balance.component';
import { PurchaseAccountBalanceDetailComponent } from './purchase-account-balance-detail.component';
import { PurchaseAccountBalanceUpdateComponent } from './purchase-account-balance-update.component';
import { PurchaseAccountBalanceDeletePopupComponent } from './purchase-account-balance-delete-dialog.component';
import { IPurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';

@Injectable({ providedIn: 'root' })
export class PurchaseAccountBalanceResolve implements Resolve<IPurchaseAccountBalance> {
  constructor(private service: PurchaseAccountBalanceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPurchaseAccountBalance> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PurchaseAccountBalance>) => response.ok),
        map((purchaseAccountBalance: HttpResponse<PurchaseAccountBalance>) => purchaseAccountBalance.body)
      );
    }
    return of(new PurchaseAccountBalance());
  }
}

export const purchaseAccountBalanceRoute: Routes = [
  {
    path: '',
    component: PurchaseAccountBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PurchaseAccountBalanceDetailComponent,
    resolve: {
      purchaseAccountBalance: PurchaseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PurchaseAccountBalanceUpdateComponent,
    resolve: {
      purchaseAccountBalance: PurchaseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PurchaseAccountBalanceUpdateComponent,
    resolve: {
      purchaseAccountBalance: PurchaseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const purchaseAccountBalancePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PurchaseAccountBalanceDeletePopupComponent,
    resolve: {
      purchaseAccountBalance: PurchaseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
