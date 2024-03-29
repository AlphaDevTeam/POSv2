import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Job } from 'app/shared/model/job.model';
import { JobService } from './job.service';
import { JobComponent } from './job.component';
import { JobDetailComponent } from './job-detail.component';
import { JobUpdateComponent } from './job-update.component';
import { JobDeletePopupComponent } from './job-delete-dialog.component';
import { IJob } from 'app/shared/model/job.model';

@Injectable({ providedIn: 'root' })
export class JobResolve implements Resolve<IJob> {
  constructor(private service: JobService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IJob> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Job>) => response.ok),
        map((job: HttpResponse<Job>) => job.body)
      );
    }
    return of(new Job());
  }
}

export const jobRoute: Routes = [
  {
    path: '',
    component: JobComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jobs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JobDetailComponent,
    resolve: {
      job: JobResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jobs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JobUpdateComponent,
    resolve: {
      job: JobResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jobs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JobUpdateComponent,
    resolve: {
      job: JobResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jobs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const jobPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: JobDeletePopupComponent,
    resolve: {
      job: JobResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Jobs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
