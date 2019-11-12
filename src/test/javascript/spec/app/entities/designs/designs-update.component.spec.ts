import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { DesignsUpdateComponent } from 'app/entities/designs/designs-update.component';
import { DesignsService } from 'app/entities/designs/designs.service';
import { Designs } from 'app/shared/model/designs.model';

describe('Component Tests', () => {
  describe('Designs Management Update Component', () => {
    let comp: DesignsUpdateComponent;
    let fixture: ComponentFixture<DesignsUpdateComponent>;
    let service: DesignsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DesignsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DesignsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DesignsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DesignsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Designs(123);
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
        const entity = new Designs();
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
