import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseOrderComponent } from 'app/entities/purchase-order/purchase-order.component';
import { PurchaseOrderService } from 'app/entities/purchase-order/purchase-order.service';
import { PurchaseOrder } from 'app/shared/model/purchase-order.model';

describe('Component Tests', () => {
  describe('PurchaseOrder Management Component', () => {
    let comp: PurchaseOrderComponent;
    let fixture: ComponentFixture<PurchaseOrderComponent>;
    let service: PurchaseOrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseOrderComponent],
        providers: []
      })
        .overrideTemplate(PurchaseOrderComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaseOrderComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseOrderService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PurchaseOrder(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.purchaseOrders[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
