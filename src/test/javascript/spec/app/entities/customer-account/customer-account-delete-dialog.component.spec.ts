import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { CustomerAccountDeleteDialogComponent } from 'app/entities/customer-account/customer-account-delete-dialog.component';
import { CustomerAccountService } from 'app/entities/customer-account/customer-account.service';

describe('Component Tests', () => {
  describe('CustomerAccount Management Delete Component', () => {
    let comp: CustomerAccountDeleteDialogComponent;
    let fixture: ComponentFixture<CustomerAccountDeleteDialogComponent>;
    let service: CustomerAccountService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CustomerAccountDeleteDialogComponent]
      })
        .overrideTemplate(CustomerAccountDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomerAccountDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerAccountService);
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
