import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { DesingsComponent } from './desings.component';
import { DesingsDetailComponent } from './desings-detail.component';
import { DesingsUpdateComponent } from './desings-update.component';
import { DesingsDeletePopupComponent, DesingsDeleteDialogComponent } from './desings-delete-dialog.component';
import { desingsRoute, desingsPopupRoute } from './desings.route';

const ENTITY_STATES = [...desingsRoute, ...desingsPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DesingsComponent,
    DesingsDetailComponent,
    DesingsUpdateComponent,
    DesingsDeleteDialogComponent,
    DesingsDeletePopupComponent
  ],
  entryComponents: [DesingsDeleteDialogComponent]
})
export class PoSv2DesingsModule {}
