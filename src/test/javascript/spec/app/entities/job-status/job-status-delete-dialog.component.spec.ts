import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { JobStatusDeleteDialogComponent } from 'app/entities/job-status/job-status-delete-dialog.component';
import { JobStatusService } from 'app/entities/job-status/job-status.service';

describe('Component Tests', () => {
  describe('JobStatus Management Delete Component', () => {
    let comp: JobStatusDeleteDialogComponent;
    let fixture: ComponentFixture<JobStatusDeleteDialogComponent>;
    let service: JobStatusService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [JobStatusDeleteDialogComponent]
      })
        .overrideTemplate(JobStatusDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobStatusDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobStatusService);
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
