import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseAccountBalanceDeleteDialogComponent } from 'app/entities/purchase-account-balance/purchase-account-balance-delete-dialog.component';
import { PurchaseAccountBalanceService } from 'app/entities/purchase-account-balance/purchase-account-balance.service';

describe('Component Tests', () => {
  describe('PurchaseAccountBalance Management Delete Component', () => {
    let comp: PurchaseAccountBalanceDeleteDialogComponent;
    let fixture: ComponentFixture<PurchaseAccountBalanceDeleteDialogComponent>;
    let service: PurchaseAccountBalanceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseAccountBalanceDeleteDialogComponent]
      })
        .overrideTemplate(PurchaseAccountBalanceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaseAccountBalanceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseAccountBalanceService);
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
