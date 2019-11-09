import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { CashBookDeleteDialogComponent } from 'app/entities/cash-book/cash-book-delete-dialog.component';
import { CashBookService } from 'app/entities/cash-book/cash-book.service';

describe('Component Tests', () => {
  describe('CashBook Management Delete Component', () => {
    let comp: CashBookDeleteDialogComponent;
    let fixture: ComponentFixture<CashBookDeleteDialogComponent>;
    let service: CashBookService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CashBookDeleteDialogComponent]
      })
        .overrideTemplate(CashBookDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashBookDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashBookService);
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
