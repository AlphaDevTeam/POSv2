import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { DesignsComponent } from './designs.component';
import { DesignsDetailComponent } from './designs-detail.component';
import { DesignsUpdateComponent } from './designs-update.component';
import { DesignsDeletePopupComponent, DesignsDeleteDialogComponent } from './designs-delete-dialog.component';
import { designsRoute, designsPopupRoute } from './designs.route';

const ENTITY_STATES = [...designsRoute, ...designsPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DesignsComponent,
    DesignsDetailComponent,
    DesignsUpdateComponent,
    DesignsDeleteDialogComponent,
    DesignsDeletePopupComponent
  ],
  entryComponents: [DesignsDeleteDialogComponent]
})
export class PoSv2DesignsModule {}
