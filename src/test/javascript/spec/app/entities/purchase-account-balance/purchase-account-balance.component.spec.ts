import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseAccountBalanceComponent } from 'app/entities/purchase-account-balance/purchase-account-balance.component';
import { PurchaseAccountBalanceService } from 'app/entities/purchase-account-balance/purchase-account-balance.service';
import { PurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';

describe('Component Tests', () => {
  describe('PurchaseAccountBalance Management Component', () => {
    let comp: PurchaseAccountBalanceComponent;
    let fixture: ComponentFixture<PurchaseAccountBalanceComponent>;
    let service: PurchaseAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseAccountBalanceComponent],
        providers: []
      })
        .overrideTemplate(PurchaseAccountBalanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaseAccountBalanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseAccountBalanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PurchaseAccountBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.purchaseAccountBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
