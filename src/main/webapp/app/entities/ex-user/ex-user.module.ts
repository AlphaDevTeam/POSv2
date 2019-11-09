import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { ExUserComponent } from './ex-user.component';
import { ExUserDetailComponent } from './ex-user-detail.component';
import { ExUserUpdateComponent } from './ex-user-update.component';
import { ExUserDeletePopupComponent, ExUserDeleteDialogComponent } from './ex-user-delete-dialog.component';
import { exUserRoute, exUserPopupRoute } from './ex-user.route';

const ENTITY_STATES = [...exUserRoute, ...exUserPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ExUserComponent, ExUserDetailComponent, ExUserUpdateComponent, ExUserDeleteDialogComponent, ExUserDeletePopupComponent],
  entryComponents: [ExUserDeleteDialogComponent]
})
export class PoSv2ExUserModule {}
