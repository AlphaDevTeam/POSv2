import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { WorkerDetailComponent } from 'app/entities/worker/worker-detail.component';
import { Worker } from 'app/shared/model/worker.model';

describe('Component Tests', () => {
  describe('Worker Management Detail Component', () => {
    let comp: WorkerDetailComponent;
    let fixture: ComponentFixture<WorkerDetailComponent>;
    const route = ({ data: of({ worker: new Worker(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [WorkerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WorkerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.worker).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
