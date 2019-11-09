import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { GoodsReceiptDetailComponent } from 'app/entities/goods-receipt/goods-receipt-detail.component';
import { GoodsReceipt } from 'app/shared/model/goods-receipt.model';

describe('Component Tests', () => {
  describe('GoodsReceipt Management Detail Component', () => {
    let comp: GoodsReceiptDetailComponent;
    let fixture: ComponentFixture<GoodsReceiptDetailComponent>;
    const route = ({ data: of({ goodsReceipt: new GoodsReceipt(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [GoodsReceiptDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GoodsReceiptDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GoodsReceiptDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.goodsReceipt).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
