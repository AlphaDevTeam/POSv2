import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { ItemsComponent } from './items.component';
import { ItemsDetailComponent } from './items-detail.component';
import { ItemsUpdateComponent } from './items-update.component';
import { ItemsDeletePopupComponent, ItemsDeleteDialogComponent } from './items-delete-dialog.component';
import { itemsRoute, itemsPopupRoute } from './items.route';

const ENTITY_STATES = [...itemsRoute, ...itemsPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ItemsComponent, ItemsDetailComponent, ItemsUpdateComponent, ItemsDeleteDialogComponent, ItemsDeletePopupComponent],
  entryComponents: [ItemsDeleteDialogComponent]
})
export class PoSv2ItemsModule {}
