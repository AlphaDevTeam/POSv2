import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JobDetais } from 'app/shared/model/job-detais.model';
import { JobDetaisService } from './job-detais.service';
import { JobDetaisComponent } from './job-detais.component';
import { JobDetaisDetailComponent } from './job-detais-detail.component';
import { JobDetaisUpdateComponent } from './job-detais-update.component';
import { JobDetaisDeletePopupComponent } from './job-detais-delete-dialog.component';
import { IJobDetais } from 'app/shared/model/job-detais.model';

@Injectable({ providedIn: 'root' })
export class JobDetaisResolve implements Resolve<IJobDetais> {
  constructor(private service: JobDetaisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IJobDetais> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<JobDetais>) => response.ok),
        map((jobDetais: HttpResponse<JobDetais>) => jobDetais.body)
      );
    }
    return of(new JobDetais());
  }
}

export const jobDetaisRoute: Routes = [
  {
    path: '',
    component: JobDetaisComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobDetais'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JobDetaisDetailComponent,
    resolve: {
      jobDetais: JobDetaisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobDetais'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JobDetaisUpdateComponent,
    resolve: {
      jobDetais: JobDetaisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobDetais'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JobDetaisUpdateComponent,
    resolve: {
      jobDetais: JobDetaisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobDetais'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const jobDetaisPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: JobDetaisDeletePopupComponent,
    resolve: {
      jobDetais: JobDetaisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobDetais'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
