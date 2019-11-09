import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseOrderDetailsUpdateComponent } from 'app/entities/purchase-order-details/purchase-order-details-update.component';
import { PurchaseOrderDetailsService } from 'app/entities/purchase-order-details/purchase-order-details.service';
import { PurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';

describe('Component Tests', () => {
  describe('PurchaseOrderDetails Management Update Component', () => {
    let comp: PurchaseOrderDetailsUpdateComponent;
    let fixture: ComponentFixture<PurchaseOrderDetailsUpdateComponent>;
    let service: PurchaseOrderDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseOrderDetailsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PurchaseOrderDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaseOrderDetailsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseOrderDetailsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PurchaseOrderDetails(123);
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
        const entity = new PurchaseOrderDetails();
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
