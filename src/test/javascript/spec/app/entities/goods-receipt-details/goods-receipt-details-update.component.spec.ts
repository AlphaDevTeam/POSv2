import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { GoodsReceiptDetailsUpdateComponent } from 'app/entities/goods-receipt-details/goods-receipt-details-update.component';
import { GoodsReceiptDetailsService } from 'app/entities/goods-receipt-details/goods-receipt-details.service';
import { GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

describe('Component Tests', () => {
  describe('GoodsReceiptDetails Management Update Component', () => {
    let comp: GoodsReceiptDetailsUpdateComponent;
    let fixture: ComponentFixture<GoodsReceiptDetailsUpdateComponent>;
    let service: GoodsReceiptDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [GoodsReceiptDetailsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GoodsReceiptDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoodsReceiptDetailsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodsReceiptDetailsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GoodsReceiptDetails(123);
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
        const entity = new GoodsReceiptDetails();
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
