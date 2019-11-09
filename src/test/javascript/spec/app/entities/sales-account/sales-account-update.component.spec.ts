import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { SalesAccountUpdateComponent } from 'app/entities/sales-account/sales-account-update.component';
import { SalesAccountService } from 'app/entities/sales-account/sales-account.service';
import { SalesAccount } from 'app/shared/model/sales-account.model';

describe('Component Tests', () => {
  describe('SalesAccount Management Update Component', () => {
    let comp: SalesAccountUpdateComponent;
    let fixture: ComponentFixture<SalesAccountUpdateComponent>;
    let service: SalesAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SalesAccountUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SalesAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SalesAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SalesAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SalesAccount(123);
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
        const entity = new SalesAccount();
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
