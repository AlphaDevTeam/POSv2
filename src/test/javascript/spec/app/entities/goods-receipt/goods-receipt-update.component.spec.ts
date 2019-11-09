import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { GoodsReceiptUpdateComponent } from 'app/entities/goods-receipt/goods-receipt-update.component';
import { GoodsReceiptService } from 'app/entities/goods-receipt/goods-receipt.service';
import { GoodsReceipt } from 'app/shared/model/goods-receipt.model';

describe('Component Tests', () => {
  describe('GoodsReceipt Management Update Component', () => {
    let comp: GoodsReceiptUpdateComponent;
    let fixture: ComponentFixture<GoodsReceiptUpdateComponent>;
    let service: GoodsReceiptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [GoodsReceiptUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GoodsReceiptUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoodsReceiptUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodsReceiptService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GoodsReceipt(123);
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
        const entity = new GoodsReceipt();
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
