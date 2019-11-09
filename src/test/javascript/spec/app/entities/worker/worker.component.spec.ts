import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { WorkerComponent } from 'app/entities/worker/worker.component';
import { WorkerService } from 'app/entities/worker/worker.service';
import { Worker } from 'app/shared/model/worker.model';

describe('Component Tests', () => {
  describe('Worker Management Component', () => {
    let comp: WorkerComponent;
    let fixture: ComponentFixture<WorkerComponent>;
    let service: WorkerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [WorkerComponent],
        providers: []
      })
        .overrideTemplate(WorkerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkerComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkerService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Worker(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.workers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
