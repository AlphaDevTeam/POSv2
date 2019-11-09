import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { GoodsReceiptDeleteDialogComponent } from 'app/entities/goods-receipt/goods-receipt-delete-dialog.component';
import { GoodsReceiptService } from 'app/entities/goods-receipt/goods-receipt.service';

describe('Component Tests', () => {
  describe('GoodsReceipt Management Delete Component', () => {
    let comp: GoodsReceiptDeleteDialogComponent;
    let fixture: ComponentFixture<GoodsReceiptDeleteDialogComponent>;
    let service: GoodsReceiptService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [GoodsReceiptDeleteDialogComponent]
      })
        .overrideTemplate(GoodsReceiptDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GoodsReceiptDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodsReceiptService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
