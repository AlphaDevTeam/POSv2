import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { SupplierAccountBalanceDeleteDialogComponent } from 'app/entities/supplier-account-balance/supplier-account-balance-delete-dialog.component';
import { SupplierAccountBalanceService } from 'app/entities/supplier-account-balance/supplier-account-balance.service';

describe('Component Tests', () => {
  describe('SupplierAccountBalance Management Delete Component', () => {
    let comp: SupplierAccountBalanceDeleteDialogComponent;
    let fixture: ComponentFixture<SupplierAccountBalanceDeleteDialogComponent>;
    let service: SupplierAccountBalanceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SupplierAccountBalanceDeleteDialogComponent]
      })
        .overrideTemplate(SupplierAccountBalanceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SupplierAccountBalanceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SupplierAccountBalanceService);
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
