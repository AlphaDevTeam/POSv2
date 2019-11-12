import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'products',
        loadChildren: () => import('./products/products.module').then(m => m.PoSv2ProductsModule)
      },
      {
        path: 'desings',
        loadChildren: () => import('./desings/desings.module').then(m => m.PoSv2DesingsModule)
      },
      {
        path: 'job',
        loadChildren: () => import('./job/job.module').then(m => m.PoSv2JobModule)
      },
      {
        path: 'job-detais',
        loadChildren: () => import('./job-detais/job-detais.module').then(m => m.PoSv2JobDetaisModule)
      },
      {
        path: 'job-status',
        loadChildren: () => import('./job-status/job-status.module').then(m => m.PoSv2JobStatusModule)
      },
      {
        path: 'items',
        loadChildren: () => import('./items/items.module').then(m => m.PoSv2ItemsModule)
      },
      {
        path: 'item-bin-card',
        loadChildren: () => import('./item-bin-card/item-bin-card.module').then(m => m.PoSv2ItemBinCardModule)
      },
      {
        path: 'purchase-order',
        loadChildren: () => import('./purchase-order/purchase-order.module').then(m => m.PoSv2PurchaseOrderModule)
      },
      {
        path: 'purchase-order-details',
        loadChildren: () => import('./purchase-order-details/purchase-order-details.module').then(m => m.PoSv2PurchaseOrderDetailsModule)
      },
      {
        path: 'goods-receipt',
        loadChildren: () => import('./goods-receipt/goods-receipt.module').then(m => m.PoSv2GoodsReceiptModule)
      },
      {
        path: 'goods-receipt-details',
        loadChildren: () => import('./goods-receipt-details/goods-receipt-details.module').then(m => m.PoSv2GoodsReceiptDetailsModule)
      },
      {
        path: 'cash-book',
        loadChildren: () => import('./cash-book/cash-book.module').then(m => m.PoSv2CashBookModule)
      },
      {
        path: 'cash-book-balance',
        loadChildren: () => import('./cash-book-balance/cash-book-balance.module').then(m => m.PoSv2CashBookBalanceModule)
      },
      {
        path: 'customer-account',
        loadChildren: () => import('./customer-account/customer-account.module').then(m => m.PoSv2CustomerAccountModule)
      },
      {
        path: 'customer-account-balance',
        loadChildren: () =>
          import('./customer-account-balance/customer-account-balance.module').then(m => m.PoSv2CustomerAccountBalanceModule)
      },
      {
        path: 'supplier-account',
        loadChildren: () => import('./supplier-account/supplier-account.module').then(m => m.PoSv2SupplierAccountModule)
      },
      {
        path: 'supplier-account-balance',
        loadChildren: () =>
          import('./supplier-account-balance/supplier-account-balance.module').then(m => m.PoSv2SupplierAccountBalanceModule)
      },
      {
        path: 'purchase-account',
        loadChildren: () => import('./purchase-account/purchase-account.module').then(m => m.PoSv2PurchaseAccountModule)
      },
      {
        path: 'purchase-account-balance',
        loadChildren: () =>
          import('./purchase-account-balance/purchase-account-balance.module').then(m => m.PoSv2PurchaseAccountBalanceModule)
      },
      {
        path: 'sales-account',
        loadChildren: () => import('./sales-account/sales-account.module').then(m => m.PoSv2SalesAccountModule)
      },
      {
        path: 'sales-account-balance',
        loadChildren: () => import('./sales-account-balance/sales-account-balance.module').then(m => m.PoSv2SalesAccountBalanceModule)
      },
      {
        path: 'document-type',
        loadChildren: () => import('./document-type/document-type.module').then(m => m.PoSv2DocumentTypeModule)
      },
      {
        path: 'transaction-type',
        loadChildren: () => import('./transaction-type/transaction-type.module').then(m => m.PoSv2TransactionTypeModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.PoSv2LocationModule)
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.PoSv2CustomerModule)
      },
      {
        path: 'supplier',
        loadChildren: () => import('./supplier/supplier.module').then(m => m.PoSv2SupplierModule)
      },
      {
        path: 'worker',
        loadChildren: () => import('./worker/worker.module').then(m => m.PoSv2WorkerModule)
      },
      {
        path: 'ex-user',
        loadChildren: () => import('./ex-user/ex-user.module').then(m => m.PoSv2ExUserModule)
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.PoSv2StockModule)
      },
      {
        path: 'company',
        loadChildren: () => import('./company/company.module').then(m => m.PoSv2CompanyModule)
      },
      {
        path: 'designs',
        loadChildren: () => import('./designs/designs.module').then(m => m.PoSv2DesignsModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PoSv2EntityModule {}
