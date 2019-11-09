import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CashBook } from 'app/shared/model/cash-book.model';
import { CashBookService } from './cash-book.service';
import { CashBookComponent } from './cash-book.component';
import { CashBookDetailComponent } from './cash-book-detail.component';
import { CashBookUpdateComponent } from './cash-book-update.component';
import { CashBookDeletePopupComponent } from './cash-book-delete-dialog.component';
import { ICashBook } from 'app/shared/model/cash-book.model';

@Injectable({ providedIn: 'root' })
export class CashBookResolve implements Resolve<ICashBook> {
  constructor(private service: CashBookService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICashBook> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CashBook>) => response.ok),
        map((cashBook: HttpResponse<CashBook>) => cashBook.body)
      );
    }
    return of(new CashBook());
  }
}

export const cashBookRoute: Routes = [
  {
    path: '',
    component: CashBookComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashBookDetailComponent,
    resolve: {
      cashBook: CashBookResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashBookUpdateComponent,
    resolve: {
      cashBook: CashBookResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashBookUpdateComponent,
    resolve: {
      cashBook: CashBookResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cashBookPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CashBookDeletePopupComponent,
    resolve: {
      cashBook: CashBookResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
