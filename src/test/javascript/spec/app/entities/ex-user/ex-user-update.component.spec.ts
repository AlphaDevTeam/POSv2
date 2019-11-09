import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { ExUserUpdateComponent } from 'app/entities/ex-user/ex-user-update.component';
import { ExUserService } from 'app/entities/ex-user/ex-user.service';
import { ExUser } from 'app/shared/model/ex-user.model';

describe('Component Tests', () => {
  describe('ExUser Management Update Component', () => {
    let comp: ExUserUpdateComponent;
    let fixture: ComponentFixture<ExUserUpdateComponent>;
    let service: ExUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ExUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExUser(123);
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
        const entity = new ExUser();
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
