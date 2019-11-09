import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { SalesAccountBalanceComponent } from './sales-account-balance.component';
import { SalesAccountBalanceDetailComponent } from './sales-account-balance-detail.component';
import { SalesAccountBalanceUpdateComponent } from './sales-account-balance-update.component';
import {
  SalesAccountBalanceDeletePopupComponent,
  SalesAccountBalanceDeleteDialogComponent
} from './sales-account-balance-delete-dialog.component';
import { salesAccountBalanceRoute, salesAccountBalancePopupRoute } from './sales-account-balance.route';

const ENTITY_STATES = [...salesAccountBalanceRoute, ...salesAccountBalancePopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SalesAccountBalanceComponent,
    SalesAccountBalanceDetailComponent,
    SalesAccountBalanceUpdateComponent,
    SalesAccountBalanceDeleteDialogComponent,
    SalesAccountBalanceDeletePopupComponent
  ],
  entryComponents: [SalesAccountBalanceDeleteDialogComponent]
})
export class PoSv2SalesAccountBalanceModule {}
