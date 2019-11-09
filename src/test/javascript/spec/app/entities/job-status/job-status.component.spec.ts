import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { JobStatusComponent } from 'app/entities/job-status/job-status.component';
import { JobStatusService } from 'app/entities/job-status/job-status.service';
import { JobStatus } from 'app/shared/model/job-status.model';

describe('Component Tests', () => {
  describe('JobStatus Management Component', () => {
    let comp: JobStatusComponent;
    let fixture: ComponentFixture<JobStatusComponent>;
    let service: JobStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [JobStatusComponent],
        providers: []
      })
        .overrideTemplate(JobStatusComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobStatusComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobStatusService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new JobStatus(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.jobStatuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
