import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { WorkerComponent } from './worker.component';
import { WorkerDetailComponent } from './worker-detail.component';
import { WorkerUpdateComponent } from './worker-update.component';
import { WorkerDeletePopupComponent, WorkerDeleteDialogComponent } from './worker-delete-dialog.component';
import { workerRoute, workerPopupRoute } from './worker.route';

const ENTITY_STATES = [...workerRoute, ...workerPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [WorkerComponent, WorkerDetailComponent, WorkerUpdateComponent, WorkerDeleteDialogComponent, WorkerDeletePopupComponent],
  entryComponents: [WorkerDeleteDialogComponent]
})
export class PoSv2WorkerModule {}
