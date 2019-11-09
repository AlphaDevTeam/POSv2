import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PurchaseAccount } from 'app/shared/model/purchase-account.model';
import { PurchaseAccountService } from './purchase-account.service';
import { PurchaseAccountComponent } from './purchase-account.component';
import { PurchaseAccountDetailComponent } from './purchase-account-detail.component';
import { PurchaseAccountUpdateComponent } from './purchase-account-update.component';
import { PurchaseAccountDeletePopupComponent } from './purchase-account-delete-dialog.component';
import { IPurchaseAccount } from 'app/shared/model/purchase-account.model';

@Injectable({ providedIn: 'root' })
export class PurchaseAccountResolve implements Resolve<IPurchaseAccount> {
  constructor(private service: PurchaseAccountService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPurchaseAccount> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PurchaseAccount>) => response.ok),
        map((purchaseAccount: HttpResponse<PurchaseAccount>) => purchaseAccount.body)
      );
    }
    return of(new PurchaseAccount());
  }
}

export const purchaseAccountRoute: Routes = [
  {
    path: '',
    component: PurchaseAccountComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PurchaseAccountDetailComponent,
    resolve: {
      purchaseAccount: PurchaseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PurchaseAccountUpdateComponent,
    resolve: {
      purchaseAccount: PurchaseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PurchaseAccountUpdateComponent,
    resolve: {
      purchaseAccount: PurchaseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const purchaseAccountPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PurchaseAccountDeletePopupComponent,
    resolve: {
      purchaseAccount: PurchaseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
