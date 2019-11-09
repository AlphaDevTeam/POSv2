import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PoSv2SharedModule } from 'app/shared/shared.module';
import { DocumentTypeComponent } from './document-type.component';
import { DocumentTypeDetailComponent } from './document-type-detail.component';
import { DocumentTypeUpdateComponent } from './document-type-update.component';
import { DocumentTypeDeletePopupComponent, DocumentTypeDeleteDialogComponent } from './document-type-delete-dialog.component';
import { documentTypeRoute, documentTypePopupRoute } from './document-type.route';

const ENTITY_STATES = [...documentTypeRoute, ...documentTypePopupRoute];

@NgModule({
  imports: [PoSv2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DocumentTypeComponent,
    DocumentTypeDetailComponent,
    DocumentTypeUpdateComponent,
    DocumentTypeDeleteDialogComponent,
    DocumentTypeDeletePopupComponent
  ],
  entryComponents: [DocumentTypeDeleteDialogComponent]
})
export class PoSv2DocumentTypeModule {}
