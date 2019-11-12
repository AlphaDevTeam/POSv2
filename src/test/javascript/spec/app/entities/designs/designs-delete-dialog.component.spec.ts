import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { DesignsDeleteDialogComponent } from 'app/entities/designs/designs-delete-dialog.component';
import { DesignsService } from 'app/entities/designs/designs.service';

describe('Component Tests', () => {
  describe('Designs Management Delete Component', () => {
    let comp: DesignsDeleteDialogComponent;
    let fixture: ComponentFixture<DesignsDeleteDialogComponent>;
    let service: DesignsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DesignsDeleteDialogComponent]
      })
        .overrideTemplate(DesignsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DesignsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DesignsService);
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
