import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { PurchaseOrderDetailsComponent } from './purchase-order-details.component';
import { PurchaseOrderDetailsDetailComponent } from './purchase-order-details-detail.component';
import { PurchaseOrderDetailsUpdateComponent } from './purchase-order-details-update.component';
import {
  PurchaseOrderDetailsDeletePopupComponent,
  PurchaseOrderDetailsDeleteDialogComponent
} from './purchase-order-details-delete-dialog.component';
import { purchaseOrderDetailsRoute, purchaseOrderDetailsPopupRoute } from './purchase-order-details.route';

const ENTITY_STATES = [...purchaseOrderDetailsRoute, ...purchaseOrderDetailsPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PurchaseOrderDetailsComponent,
    PurchaseOrderDetailsDetailComponent,
    PurchaseOrderDetailsUpdateComponent,
    PurchaseOrderDetailsDeleteDialogComponent,
    PurchaseOrderDetailsDeletePopupComponent
  ],
  entryComponents: [PurchaseOrderDetailsDeleteDialogComponent]
})
export class PoSv2PurchaseOrderDetailsModule {}
