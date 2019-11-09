import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Desings } from 'app/shared/model/desings.model';
import { DesingsService } from './desings.service';
import { DesingsComponent } from './desings.component';
import { DesingsDetailComponent } from './desings-detail.component';
import { DesingsUpdateComponent } from './desings-update.component';
import { DesingsDeletePopupComponent } from './desings-delete-dialog.component';
import { IDesings } from 'app/shared/model/desings.model';

@Injectable({ providedIn: 'root' })
export class DesingsResolve implements Resolve<IDesings> {
  constructor(private service: DesingsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDesings> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Desings>) => response.ok),
        map((desings: HttpResponse<Desings>) => desings.body)
      );
    }
    return of(new Desings());
  }
}

export const desingsRoute: Routes = [
  {
    path: '',
    component: DesingsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Desings'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DesingsDetailComponent,
    resolve: {
      desings: DesingsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Desings'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DesingsUpdateComponent,
    resolve: {
      desings: DesingsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Desings'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DesingsUpdateComponent,
    resolve: {
      desings: DesingsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Desings'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const desingsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DesingsDeletePopupComponent,
    resolve: {
      desings: DesingsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Desings'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
