import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { GoodsReceiptDetailsDeleteDialogComponent } from 'app/entities/goods-receipt-details/goods-receipt-details-delete-dialog.component';
import { GoodsReceiptDetailsService } from 'app/entities/goods-receipt-details/goods-receipt-details.service';

describe('Component Tests', () => {
  describe('GoodsReceiptDetails Management Delete Component', () => {
    let comp: GoodsReceiptDetailsDeleteDialogComponent;
    let fixture: ComponentFixture<GoodsReceiptDetailsDeleteDialogComponent>;
    let service: GoodsReceiptDetailsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [GoodsReceiptDetailsDeleteDialogComponent]
      })
        .overrideTemplate(GoodsReceiptDetailsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GoodsReceiptDetailsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodsReceiptDetailsService);
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
