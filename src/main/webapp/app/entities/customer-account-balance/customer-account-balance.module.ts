import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { CustomerAccountBalanceComponent } from './customer-account-balance.component';
import { CustomerAccountBalanceDetailComponent } from './customer-account-balance-detail.component';
import { CustomerAccountBalanceUpdateComponent } from './customer-account-balance-update.component';
import {
  CustomerAccountBalanceDeletePopupComponent,
  CustomerAccountBalanceDeleteDialogComponent
} from './customer-account-balance-delete-dialog.component';
import { customerAccountBalanceRoute, customerAccountBalancePopupRoute } from './customer-account-balance.route';

const ENTITY_STATES = [...customerAccountBalanceRoute, ...customerAccountBalancePopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CustomerAccountBalanceComponent,
    CustomerAccountBalanceDetailComponent,
    CustomerAccountBalanceUpdateComponent,
    CustomerAccountBalanceDeleteDialogComponent,
    CustomerAccountBalanceDeletePopupComponent
  ],
  entryComponents: [CustomerAccountBalanceDeleteDialogComponent]
})
export class PoSv2CustomerAccountBalanceModule {}
