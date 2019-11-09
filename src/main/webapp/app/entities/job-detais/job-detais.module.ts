import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { JobDetaisComponent } from './job-detais.component';
import { JobDetaisDetailComponent } from './job-detais-detail.component';
import { JobDetaisUpdateComponent } from './job-detais-update.component';
import { JobDetaisDeletePopupComponent, JobDetaisDeleteDialogComponent } from './job-detais-delete-dialog.component';
import { jobDetaisRoute, jobDetaisPopupRoute } from './job-detais.route';

const ENTITY_STATES = [...jobDetaisRoute, ...jobDetaisPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    JobDetaisComponent,
    JobDetaisDetailComponent,
    JobDetaisUpdateComponent,
    JobDetaisDeleteDialogComponent,
    JobDetaisDeletePopupComponent
  ],
  entryComponents: [JobDetaisDeleteDialogComponent]
})
export class PoSv2JobDetaisModule {}
