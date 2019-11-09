import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { SupplierAccountBalanceComponent } from './supplier-account-balance.component';
import { SupplierAccountBalanceDetailComponent } from './supplier-account-balance-detail.component';
import { SupplierAccountBalanceUpdateComponent } from './supplier-account-balance-update.component';
import {
  SupplierAccountBalanceDeletePopupComponent,
  SupplierAccountBalanceDeleteDialogComponent
} from './supplier-account-balance-delete-dialog.component';
import { supplierAccountBalanceRoute, supplierAccountBalancePopupRoute } from './supplier-account-balance.route';

const ENTITY_STATES = [...supplierAccountBalanceRoute, ...supplierAccountBalancePopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SupplierAccountBalanceComponent,
    SupplierAccountBalanceDetailComponent,
    SupplierAccountBalanceUpdateComponent,
    SupplierAccountBalanceDeleteDialogComponent,
    SupplierAccountBalanceDeletePopupComponent
  ],
  entryComponents: [SupplierAccountBalanceDeleteDialogComponent]
})
export class PoSv2SupplierAccountBalanceModule {}
