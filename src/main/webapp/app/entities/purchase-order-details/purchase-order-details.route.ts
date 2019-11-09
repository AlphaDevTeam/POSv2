import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';
import { PurchaseOrderDetailsService } from './purchase-order-details.service';
import { PurchaseOrderDetailsComponent } from './purchase-order-details.component';
import { PurchaseOrderDetailsDetailComponent } from './purchase-order-details-detail.component';
import { PurchaseOrderDetailsUpdateComponent } from './purchase-order-details-update.component';
import { PurchaseOrderDetailsDeletePopupComponent } from './purchase-order-details-delete-dialog.component';
import { IPurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';

@Injectable({ providedIn: 'root' })
export class PurchaseOrderDetailsResolve implements Resolve<IPurchaseOrderDetails> {
  constructor(private service: PurchaseOrderDetailsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPurchaseOrderDetails> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PurchaseOrderDetails>) => response.ok),
        map((purchaseOrderDetails: HttpResponse<PurchaseOrderDetails>) => purchaseOrderDetails.body)
      );
    }
    return of(new PurchaseOrderDetails());
  }
}

export const purchaseOrderDetailsRoute: Routes = [
  {
    path: '',
    component: PurchaseOrderDetailsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PurchaseOrderDetailsDetailComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PurchaseOrderDetailsUpdateComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PurchaseOrderDetailsUpdateComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const purchaseOrderDetailsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PurchaseOrderDetailsDeletePopupComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
