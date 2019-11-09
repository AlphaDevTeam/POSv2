import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { GoodsReceiptComponent } from 'app/entities/goods-receipt/goods-receipt.component';
import { GoodsReceiptService } from 'app/entities/goods-receipt/goods-receipt.service';
import { GoodsReceipt } from 'app/shared/model/goods-receipt.model';

describe('Component Tests', () => {
  describe('GoodsReceipt Management Component', () => {
    let comp: GoodsReceiptComponent;
    let fixture: ComponentFixture<GoodsReceiptComponent>;
    let service: GoodsReceiptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [GoodsReceiptComponent],
        providers: []
      })
        .overrideTemplate(GoodsReceiptComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoodsReceiptComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodsReceiptService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new GoodsReceipt(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.goodsReceipts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
