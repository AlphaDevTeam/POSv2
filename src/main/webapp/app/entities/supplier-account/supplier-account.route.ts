import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SupplierAccount } from 'app/shared/model/supplier-account.model';
import { SupplierAccountService } from './supplier-account.service';
import { SupplierAccountComponent } from './supplier-account.component';
import { SupplierAccountDetailComponent } from './supplier-account-detail.component';
import { SupplierAccountUpdateComponent } from './supplier-account-update.component';
import { SupplierAccountDeletePopupComponent } from './supplier-account-delete-dialog.component';
import { ISupplierAccount } from 'app/shared/model/supplier-account.model';

@Injectable({ providedIn: 'root' })
export class SupplierAccountResolve implements Resolve<ISupplierAccount> {
  constructor(private service: SupplierAccountService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISupplierAccount> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SupplierAccount>) => response.ok),
        map((supplierAccount: HttpResponse<SupplierAccount>) => supplierAccount.body)
      );
    }
    return of(new SupplierAccount());
  }
}

export const supplierAccountRoute: Routes = [
  {
    path: '',
    component: SupplierAccountComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SupplierAccountDetailComponent,
    resolve: {
      supplierAccount: SupplierAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SupplierAccountUpdateComponent,
    resolve: {
      supplierAccount: SupplierAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SupplierAccountUpdateComponent,
    resolve: {
      supplierAccount: SupplierAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const supplierAccountPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SupplierAccountDeletePopupComponent,
    resolve: {
      supplierAccount: SupplierAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
