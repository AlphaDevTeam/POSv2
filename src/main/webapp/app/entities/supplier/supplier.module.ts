import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { SupplierComponent } from './supplier.component';
import { SupplierDetailComponent } from './supplier-detail.component';
import { SupplierUpdateComponent } from './supplier-update.component';
import { SupplierDeletePopupComponent, SupplierDeleteDialogComponent } from './supplier-delete-dialog.component';
import { supplierRoute, supplierPopupRoute } from './supplier.route';

const ENTITY_STATES = [...supplierRoute, ...supplierPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SupplierComponent,
    SupplierDetailComponent,
    SupplierUpdateComponent,
    SupplierDeleteDialogComponent,
    SupplierDeletePopupComponent
  ],
  entryComponents: [SupplierDeleteDialogComponent]
})
export class PoSv2SupplierModule {}
