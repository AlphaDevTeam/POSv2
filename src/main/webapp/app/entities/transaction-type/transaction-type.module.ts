import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { TransactionTypeComponent } from './transaction-type.component';
import { TransactionTypeDetailComponent } from './transaction-type-detail.component';
import { TransactionTypeUpdateComponent } from './transaction-type-update.component';
import { TransactionTypeDeletePopupComponent, TransactionTypeDeleteDialogComponent } from './transaction-type-delete-dialog.component';
import { transactionTypeRoute, transactionTypePopupRoute } from './transaction-type.route';

const ENTITY_STATES = [...transactionTypeRoute, ...transactionTypePopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TransactionTypeComponent,
    TransactionTypeDetailComponent,
    TransactionTypeUpdateComponent,
    TransactionTypeDeleteDialogComponent,
    TransactionTypeDeletePopupComponent
  ],
  entryComponents: [TransactionTypeDeleteDialogComponent]
})
export class PoSv2TransactionTypeModule {}
