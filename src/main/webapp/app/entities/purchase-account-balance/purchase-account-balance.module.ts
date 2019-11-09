import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { PurchaseAccountBalanceComponent } from './purchase-account-balance.component';
import { PurchaseAccountBalanceDetailComponent } from './purchase-account-balance-detail.component';
import { PurchaseAccountBalanceUpdateComponent } from './purchase-account-balance-update.component';
import {
  PurchaseAccountBalanceDeletePopupComponent,
  PurchaseAccountBalanceDeleteDialogComponent
} from './purchase-account-balance-delete-dialog.component';
import { purchaseAccountBalanceRoute, purchaseAccountBalancePopupRoute } from './purchase-account-balance.route';

const ENTITY_STATES = [...purchaseAccountBalanceRoute, ...purchaseAccountBalancePopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PurchaseAccountBalanceComponent,
    PurchaseAccountBalanceDetailComponent,
    PurchaseAccountBalanceUpdateComponent,
    PurchaseAccountBalanceDeleteDialogComponent,
    PurchaseAccountBalanceDeletePopupComponent
  ],
  entryComponents: [PurchaseAccountBalanceDeleteDialogComponent]
})
export class PoSv2PurchaseAccountBalanceModule {}
