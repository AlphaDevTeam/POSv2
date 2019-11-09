import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { DesingsUpdateComponent } from 'app/entities/desings/desings-update.component';
import { DesingsService } from 'app/entities/desings/desings.service';
import { Desings } from 'app/shared/model/desings.model';

describe('Component Tests', () => {
  describe('Desings Management Update Component', () => {
    let comp: DesingsUpdateComponent;
    let fixture: ComponentFixture<DesingsUpdateComponent>;
    let service: DesingsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DesingsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DesingsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DesingsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DesingsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Desings(123);
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
        const entity = new Desings();
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
