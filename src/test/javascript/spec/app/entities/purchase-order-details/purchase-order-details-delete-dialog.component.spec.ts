import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseOrderDetailsDeleteDialogComponent } from 'app/entities/purchase-order-details/purchase-order-details-delete-dialog.component';
import { PurchaseOrderDetailsService } from 'app/entities/purchase-order-details/purchase-order-details.service';

describe('Component Tests', () => {
  describe('PurchaseOrderDetails Management Delete Component', () => {
    let comp: PurchaseOrderDetailsDeleteDialogComponent;
    let fixture: ComponentFixture<PurchaseOrderDetailsDeleteDialogComponent>;
    let service: PurchaseOrderDetailsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseOrderDetailsDeleteDialogComponent]
      })
        .overrideTemplate(PurchaseOrderDetailsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaseOrderDetailsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseOrderDetailsService);
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
