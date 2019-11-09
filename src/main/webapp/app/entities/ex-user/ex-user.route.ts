import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';
import { ExUserComponent } from './ex-user.component';
import { ExUserDetailComponent } from './ex-user-detail.component';
import { ExUserUpdateComponent } from './ex-user-update.component';
import { ExUserDeletePopupComponent } from './ex-user-delete-dialog.component';
import { IExUser } from 'app/shared/model/ex-user.model';

@Injectable({ providedIn: 'root' })
export class ExUserResolve implements Resolve<IExUser> {
  constructor(private service: ExUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExUser> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ExUser>) => response.ok),
        map((exUser: HttpResponse<ExUser>) => exUser.body)
      );
    }
    return of(new ExUser());
  }
}

export const exUserRoute: Routes = [
  {
    path: '',
    component: ExUserComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExUserDetailComponent,
    resolve: {
      exUser: ExUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExUserUpdateComponent,
    resolve: {
      exUser: ExUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExUserUpdateComponent,
    resolve: {
      exUser: ExUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const exUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ExUserDeletePopupComponent,
    resolve: {
      exUser: ExUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
