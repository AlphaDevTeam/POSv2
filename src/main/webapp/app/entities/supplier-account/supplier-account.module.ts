import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { SupplierAccountComponent } from './supplier-account.component';
import { SupplierAccountDetailComponent } from './supplier-account-detail.component';
import { SupplierAccountUpdateComponent } from './supplier-account-update.component';
import { SupplierAccountDeletePopupComponent, SupplierAccountDeleteDialogComponent } from './supplier-account-delete-dialog.component';
import { supplierAccountRoute, supplierAccountPopupRoute } from './supplier-account.route';

const ENTITY_STATES = [...supplierAccountRoute, ...supplierAccountPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SupplierAccountComponent,
    SupplierAccountDetailComponent,
    SupplierAccountUpdateComponent,
    SupplierAccountDeleteDialogComponent,
    SupplierAccountDeletePopupComponent
  ],
  entryComponents: [SupplierAccountDeleteDialogComponent]
})
export class PoSv2SupplierAccountModule {}
