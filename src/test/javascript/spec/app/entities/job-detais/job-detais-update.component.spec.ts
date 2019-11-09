import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { JobDetaisUpdateComponent } from 'app/entities/job-detais/job-detais-update.component';
import { JobDetaisService } from 'app/entities/job-detais/job-detais.service';
import { JobDetais } from 'app/shared/model/job-detais.model';

describe('Component Tests', () => {
  describe('JobDetais Management Update Component', () => {
    let comp: JobDetaisUpdateComponent;
    let fixture: ComponentFixture<JobDetaisUpdateComponent>;
    let service: JobDetaisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [JobDetaisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JobDetaisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobDetaisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobDetaisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JobDetais(123);
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
        const entity = new JobDetais();
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
