import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { CustomerAccountBalanceDeleteDialogComponent } from 'app/entities/customer-account-balance/customer-account-balance-delete-dialog.component';
import { CustomerAccountBalanceService } from 'app/entities/customer-account-balance/customer-account-balance.service';

describe('Component Tests', () => {
  describe('CustomerAccountBalance Management Delete Component', () => {
    let comp: CustomerAccountBalanceDeleteDialogComponent;
    let fixture: ComponentFixture<CustomerAccountBalanceDeleteDialogComponent>;
    let service: CustomerAccountBalanceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CustomerAccountBalanceDeleteDialogComponent]
      })
        .overrideTemplate(CustomerAccountBalanceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomerAccountBalanceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerAccountBalanceService);
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
