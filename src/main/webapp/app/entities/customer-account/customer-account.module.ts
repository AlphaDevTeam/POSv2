import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { CustomerAccountComponent } from './customer-account.component';
import { CustomerAccountDetailComponent } from './customer-account-detail.component';
import { CustomerAccountUpdateComponent } from './customer-account-update.component';
import { CustomerAccountDeletePopupComponent, CustomerAccountDeleteDialogComponent } from './customer-account-delete-dialog.component';
import { customerAccountRoute, customerAccountPopupRoute } from './customer-account.route';

const ENTITY_STATES = [...customerAccountRoute, ...customerAccountPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CustomerAccountComponent,
    CustomerAccountDetailComponent,
    CustomerAccountUpdateComponent,
    CustomerAccountDeleteDialogComponent,
    CustomerAccountDeletePopupComponent
  ],
  entryComponents: [CustomerAccountDeleteDialogComponent]
})
export class PoSv2CustomerAccountModule {}
