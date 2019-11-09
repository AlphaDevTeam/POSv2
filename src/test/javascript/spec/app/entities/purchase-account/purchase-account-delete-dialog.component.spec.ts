import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseAccountDeleteDialogComponent } from 'app/entities/purchase-account/purchase-account-delete-dialog.component';
import { PurchaseAccountService } from 'app/entities/purchase-account/purchase-account.service';

describe('Component Tests', () => {
  describe('PurchaseAccount Management Delete Component', () => {
    let comp: PurchaseAccountDeleteDialogComponent;
    let fixture: ComponentFixture<PurchaseAccountDeleteDialogComponent>;
    let service: PurchaseAccountService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseAccountDeleteDialogComponent]
      })
        .overrideTemplate(PurchaseAccountDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaseAccountDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseAccountService);
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
