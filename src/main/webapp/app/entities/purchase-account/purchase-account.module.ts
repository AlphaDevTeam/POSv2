import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { PurchaseAccountComponent } from './purchase-account.component';
import { PurchaseAccountDetailComponent } from './purchase-account-detail.component';
import { PurchaseAccountUpdateComponent } from './purchase-account-update.component';
import { PurchaseAccountDeletePopupComponent, PurchaseAccountDeleteDialogComponent } from './purchase-account-delete-dialog.component';
import { purchaseAccountRoute, purchaseAccountPopupRoute } from './purchase-account.route';

const ENTITY_STATES = [...purchaseAccountRoute, ...purchaseAccountPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PurchaseAccountComponent,
    PurchaseAccountDetailComponent,
    PurchaseAccountUpdateComponent,
    PurchaseAccountDeleteDialogComponent,
    PurchaseAccountDeletePopupComponent
  ],
  entryComponents: [PurchaseAccountDeleteDialogComponent]
})
export class PoSv2PurchaseAccountModule {}
