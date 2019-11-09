import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { SupplierAccountDeleteDialogComponent } from 'app/entities/supplier-account/supplier-account-delete-dialog.component';
import { SupplierAccountService } from 'app/entities/supplier-account/supplier-account.service';

describe('Component Tests', () => {
  describe('SupplierAccount Management Delete Component', () => {
    let comp: SupplierAccountDeleteDialogComponent;
    let fixture: ComponentFixture<SupplierAccountDeleteDialogComponent>;
    let service: SupplierAccountService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SupplierAccountDeleteDialogComponent]
      })
        .overrideTemplate(SupplierAccountDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SupplierAccountDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SupplierAccountService);
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
