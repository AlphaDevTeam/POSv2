import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { GoodsReceiptDetailsComponent } from './goods-receipt-details.component';
import { GoodsReceiptDetailsDetailComponent } from './goods-receipt-details-detail.component';
import { GoodsReceiptDetailsUpdateComponent } from './goods-receipt-details-update.component';
import {
  GoodsReceiptDetailsDeletePopupComponent,
  GoodsReceiptDetailsDeleteDialogComponent
} from './goods-receipt-details-delete-dialog.component';
import { goodsReceiptDetailsRoute, goodsReceiptDetailsPopupRoute } from './goods-receipt-details.route';

const ENTITY_STATES = [...goodsReceiptDetailsRoute, ...goodsReceiptDetailsPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GoodsReceiptDetailsComponent,
    GoodsReceiptDetailsDetailComponent,
    GoodsReceiptDetailsUpdateComponent,
    GoodsReceiptDetailsDeleteDialogComponent,
    GoodsReceiptDetailsDeletePopupComponent
  ],
  entryComponents: [GoodsReceiptDetailsDeleteDialogComponent]
})
export class PoSv2GoodsReceiptDetailsModule {}
