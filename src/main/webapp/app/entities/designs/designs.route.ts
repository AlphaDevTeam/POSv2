import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Designs } from 'app/shared/model/designs.model';
import { DesignsService } from './designs.service';
import { DesignsComponent } from './designs.component';
import { DesignsDetailComponent } from './designs-detail.component';
import { DesignsUpdateComponent } from './designs-update.component';
import { DesignsDeletePopupComponent } from './designs-delete-dialog.component';
import { IDesigns } from 'app/shared/model/designs.model';

@Injectable({ providedIn: 'root' })
export class DesignsResolve implements Resolve<IDesigns> {
  constructor(private service: DesignsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDesigns> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Designs>) => response.ok),
        map((designs: HttpResponse<Designs>) => designs.body)
      );
    }
    return of(new Designs());
  }
}

export const designsRoute: Routes = [
  {
    path: '',
    component: DesignsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Designs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DesignsDetailComponent,
    resolve: {
      designs: DesignsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Designs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DesignsUpdateComponent,
    resolve: {
      designs: DesignsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Designs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DesignsUpdateComponent,
    resolve: {
      designs: DesignsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Designs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const designsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DesignsDeletePopupComponent,
    resolve: {
      designs: DesignsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Designs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
