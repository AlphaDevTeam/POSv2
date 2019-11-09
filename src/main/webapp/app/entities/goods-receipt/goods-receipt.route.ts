import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from './goods-receipt.service';
import { GoodsReceiptComponent } from './goods-receipt.component';
import { GoodsReceiptDetailComponent } from './goods-receipt-detail.component';
import { GoodsReceiptUpdateComponent } from './goods-receipt-update.component';
import { GoodsReceiptDeletePopupComponent } from './goods-receipt-delete-dialog.component';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';

@Injectable({ providedIn: 'root' })
export class GoodsReceiptResolve implements Resolve<IGoodsReceipt> {
  constructor(private service: GoodsReceiptService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGoodsReceipt> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<GoodsReceipt>) => response.ok),
        map((goodsReceipt: HttpResponse<GoodsReceipt>) => goodsReceipt.body)
      );
    }
    return of(new GoodsReceipt());
  }
}

export const goodsReceiptRoute: Routes = [
  {
    path: '',
    component: GoodsReceiptComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GoodsReceiptDetailComponent,
    resolve: {
      goodsReceipt: GoodsReceiptResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GoodsReceiptUpdateComponent,
    resolve: {
      goodsReceipt: GoodsReceiptResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GoodsReceiptUpdateComponent,
    resolve: {
      goodsReceipt: GoodsReceiptResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const goodsReceiptPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GoodsReceiptDeletePopupComponent,
    resolve: {
      goodsReceipt: GoodsReceiptResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
