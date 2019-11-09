import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { GoodsReceiptComponent } from './goods-receipt.component';
import { GoodsReceiptDetailComponent } from './goods-receipt-detail.component';
import { GoodsReceiptUpdateComponent } from './goods-receipt-update.component';
import { GoodsReceiptDeletePopupComponent, GoodsReceiptDeleteDialogComponent } from './goods-receipt-delete-dialog.component';
import { goodsReceiptRoute, goodsReceiptPopupRoute } from './goods-receipt.route';

const ENTITY_STATES = [...goodsReceiptRoute, ...goodsReceiptPopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GoodsReceiptComponent,
    GoodsReceiptDetailComponent,
    GoodsReceiptUpdateComponent,
    GoodsReceiptDeleteDialogComponent,
    GoodsReceiptDeletePopupComponent
  ],
  entryComponents: [GoodsReceiptDeleteDialogComponent]
})
export class PoSv2GoodsReceiptModule {}
