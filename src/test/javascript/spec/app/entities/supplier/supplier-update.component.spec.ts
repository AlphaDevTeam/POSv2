import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { SupplierUpdateComponent } from 'app/entities/supplier/supplier-update.component';
import { SupplierService } from 'app/entities/supplier/supplier.service';
import { Supplier } from 'app/shared/model/supplier.model';

describe('Component Tests', () => {
  describe('Supplier Management Update Component', () => {
    let comp: SupplierUpdateComponent;
    let fixture: ComponentFixture<SupplierUpdateComponent>;
    let service: SupplierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SupplierUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SupplierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SupplierUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SupplierService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Supplier(123);
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
        const entity = new Supplier();
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
