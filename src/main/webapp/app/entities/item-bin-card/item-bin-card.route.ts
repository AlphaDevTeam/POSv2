import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ItemBinCard } from 'app/shared/model/item-bin-card.model';
import { ItemBinCardService } from './item-bin-card.service';
import { ItemBinCardComponent } from './item-bin-card.component';
import { ItemBinCardDetailComponent } from './item-bin-card-detail.component';
import { ItemBinCardUpdateComponent } from './item-bin-card-update.component';
import { ItemBinCardDeletePopupComponent } from './item-bin-card-delete-dialog.component';
import { IItemBinCard } from 'app/shared/model/item-bin-card.model';

@Injectable({ providedIn: 'root' })
export class ItemBinCardResolve implements Resolve<IItemBinCard> {
  constructor(private service: ItemBinCardService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IItemBinCard> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ItemBinCard>) => response.ok),
        map((itemBinCard: HttpResponse<ItemBinCard>) => itemBinCard.body)
      );
    }
    return of(new ItemBinCard());
  }
}

export const itemBinCardRoute: Routes = [
  {
    path: '',
    component: ItemBinCardComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ItemBinCardDetailComponent,
    resolve: {
      itemBinCard: ItemBinCardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ItemBinCardUpdateComponent,
    resolve: {
      itemBinCard: ItemBinCardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ItemBinCardUpdateComponent,
    resolve: {
      itemBinCard: ItemBinCardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const itemBinCardPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ItemBinCardDeletePopupComponent,
    resolve: {
      itemBinCard: ItemBinCardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
