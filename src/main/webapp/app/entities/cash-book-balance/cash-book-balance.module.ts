import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { CashBookBalanceComponent } from './cash-book-balance.component';
import { CashBookBalanceDetailComponent } from './cash-book-balance-detail.component';
import { CashBookBalanceUpdateComponent } from './cash-book-balance-update.component';
import { CashBookBalanceDeletePopupComponent, CashBookBalanceDeleteDialogComponent } from './cash-book-balance-delete-dialog.component';
import { cashBookBalanceRoute, cashBookBalancePopupRoute } from './cash-book-balance.route';

const ENTITY_STATES = [...cashBookBalanceRoute, ...cashBookBalancePopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CashBookBalanceComponent,
    CashBookBalanceDetailComponent,
    CashBookBalanceUpdateComponent,
    CashBookBalanceDeleteDialogComponent,
    CashBookBalanceDeletePopupComponent
  ],
  entryComponents: [CashBookBalanceDeleteDialogComponent]
})
export class PoSv2CashBookBalanceModule {}
