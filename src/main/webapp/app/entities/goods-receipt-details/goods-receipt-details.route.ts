import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';
import { GoodsReceiptDetailsComponent } from './goods-receipt-details.component';
import { GoodsReceiptDetailsDetailComponent } from './goods-receipt-details-detail.component';
import { GoodsReceiptDetailsUpdateComponent } from './goods-receipt-details-update.component';
import { GoodsReceiptDetailsDeletePopupComponent } from './goods-receipt-details-delete-dialog.component';
import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

@Injectable({ providedIn: 'root' })
export class GoodsReceiptDetailsResolve implements Resolve<IGoodsReceiptDetails> {
  constructor(private service: GoodsReceiptDetailsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGoodsReceiptDetails> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<GoodsReceiptDetails>) => response.ok),
        map((goodsReceiptDetails: HttpResponse<GoodsReceiptDetails>) => goodsReceiptDetails.body)
      );
    }
    return of(new GoodsReceiptDetails());
  }
}

export const goodsReceiptDetailsRoute: Routes = [
  {
    path: '',
    component: GoodsReceiptDetailsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GoodsReceiptDetailsDetailComponent,
    resolve: {
      goodsReceiptDetails: GoodsReceiptDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GoodsReceiptDetailsUpdateComponent,
    resolve: {
      goodsReceiptDetails: GoodsReceiptDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GoodsReceiptDetailsUpdateComponent,
    resolve: {
      goodsReceiptDetails: GoodsReceiptDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const goodsReceiptDetailsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GoodsReceiptDetailsDeletePopupComponent,
    resolve: {
      goodsReceiptDetails: GoodsReceiptDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
