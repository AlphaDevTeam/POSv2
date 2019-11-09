import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { WorkerUpdateComponent } from 'app/entities/worker/worker-update.component';
import { WorkerService } from 'app/entities/worker/worker.service';
import { Worker } from 'app/shared/model/worker.model';

describe('Component Tests', () => {
  describe('Worker Management Update Component', () => {
    let comp: WorkerUpdateComponent;
    let fixture: ComponentFixture<WorkerUpdateComponent>;
    let service: WorkerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [WorkerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WorkerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Worker(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Worker();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
