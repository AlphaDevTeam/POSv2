import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { GoodsReceiptDetailsComponent } from 'app/entities/goods-receipt-details/goods-receipt-details.component';
import { GoodsReceiptDetailsService } from 'app/entities/goods-receipt-details/goods-receipt-details.service';
import { GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

describe('Component Tests', () => {
  describe('GoodsReceiptDetails Management Component', () => {
    let comp: GoodsReceiptDetailsComponent;
    let fixture: ComponentFixture<GoodsReceiptDetailsComponent>;
    let service: GoodsReceiptDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [GoodsReceiptDetailsComponent],
        providers: []
      })
        .overrideTemplate(GoodsReceiptDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoodsReceiptDetailsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoodsReceiptDetailsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new GoodsReceiptDetails(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.goodsReceiptDetails[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
