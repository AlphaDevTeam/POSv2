import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { CashBookComponent } from './cash-book.component';
import { CashBookDetailComponent } from './cash-book-detail.component';
import { CashBookUpdateComponent } from './cash-book-update.component';
import { CashBookDeletePopupComponent, CashBookDeleteDialogComponent } from './cash-book-delete-dialog.component';
import { cashBookRoute, cashBookPopupRoute } from './cash-book.route';

const ENTITY_STATES = [...cashBookRoute, ...cashBookPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CashBookComponent,
    CashBookDetailComponent,
    CashBookUpdateComponent,
    CashBookDeleteDialogComponent,
    CashBookDeletePopupComponent
  ],
  entryComponents: [CashBookDeleteDialogComponent]
})
export class PoSv2CashBookModule {}
