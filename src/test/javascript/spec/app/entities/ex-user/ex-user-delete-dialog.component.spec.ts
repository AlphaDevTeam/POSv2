import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { ExUserDeleteDialogComponent } from 'app/entities/ex-user/ex-user-delete-dialog.component';
import { ExUserService } from 'app/entities/ex-user/ex-user.service';

describe('Component Tests', () => {
  describe('ExUser Management Delete Component', () => {
    let comp: ExUserDeleteDialogComponent;
    let fixture: ComponentFixture<ExUserDeleteDialogComponent>;
    let service: ExUserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ExUserDeleteDialogComponent]
      })
        .overrideTemplate(ExUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExUserService);
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
