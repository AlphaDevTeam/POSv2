import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { JobDetaisDeleteDialogComponent } from 'app/entities/job-detais/job-detais-delete-dialog.component';
import { JobDetaisService } from 'app/entities/job-detais/job-detais.service';

describe('Component Tests', () => {
  describe('JobDetais Management Delete Component', () => {
    let comp: JobDetaisDeleteDialogComponent;
    let fixture: ComponentFixture<JobDetaisDeleteDialogComponent>;
    let service: JobDetaisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [JobDetaisDeleteDialogComponent]
      })
        .overrideTemplate(JobDetaisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobDetaisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobDetaisService);
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
