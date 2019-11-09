import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { JobStatusComponent } from './job-status.component';
import { JobStatusDetailComponent } from './job-status-detail.component';
import { JobStatusUpdateComponent } from './job-status-update.component';
import { JobStatusDeletePopupComponent, JobStatusDeleteDialogComponent } from './job-status-delete-dialog.component';
import { jobStatusRoute, jobStatusPopupRoute } from './job-status.route';

const ENTITY_STATES = [...jobStatusRoute, ...jobStatusPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    JobStatusComponent,
    JobStatusDetailComponent,
    JobStatusUpdateComponent,
    JobStatusDeleteDialogComponent,
    JobStatusDeletePopupComponent
  ],
  entryComponents: [JobStatusDeleteDialogComponent]
})
export class PoSv2JobStatusModule {}
