import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { ItemBinCardComponent } from './item-bin-card.component';
import { ItemBinCardDetailComponent } from './item-bin-card-detail.component';
import { ItemBinCardUpdateComponent } from './item-bin-card-update.component';
import { ItemBinCardDeletePopupComponent, ItemBinCardDeleteDialogComponent } from './item-bin-card-delete-dialog.component';
import { itemBinCardRoute, itemBinCardPopupRoute } from './item-bin-card.route';

const ENTITY_STATES = [...itemBinCardRoute, ...itemBinCardPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ItemBinCardComponent,
    ItemBinCardDetailComponent,
    ItemBinCardUpdateComponent,
    ItemBinCardDeleteDialogComponent,
    ItemBinCardDeletePopupComponent
  ],
  entryComponents: [ItemBinCardDeleteDialogComponent]
})
export class PoSv2ItemBinCardModule {}
