import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { SalesAccountComponent } from './sales-account.component';
import { SalesAccountDetailComponent } from './sales-account-detail.component';
import { SalesAccountUpdateComponent } from './sales-account-update.component';
import { SalesAccountDeletePopupComponent, SalesAccountDeleteDialogComponent } from './sales-account-delete-dialog.component';
import { salesAccountRoute, salesAccountPopupRoute } from './sales-account.route';

const ENTITY_STATES = [...salesAccountRoute, ...salesAccountPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SalesAccountComponent,
    SalesAccountDetailComponent,
    SalesAccountUpdateComponent,
    SalesAccountDeleteDialogComponent,
    SalesAccountDeletePopupComponent
  ],
  entryComponents: [SalesAccountDeleteDialogComponent]
})
export class PoSv2SalesAccountModule {}
