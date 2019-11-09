import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { CustomerAccountBalanceComponent } from 'app/entities/customer-account-balance/customer-account-balance.component';
import { CustomerAccountBalanceService } from 'app/entities/customer-account-balance/customer-account-balance.service';
import { CustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';

describe('Component Tests', () => {
  describe('CustomerAccountBalance Management Component', () => {
    let comp: CustomerAccountBalanceComponent;
    let fixture: ComponentFixture<CustomerAccountBalanceComponent>;
    let service: CustomerAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CustomerAccountBalanceComponent],
        providers: []
      })
        .overrideTemplate(CustomerAccountBalanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerAccountBalanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerAccountBalanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CustomerAccountBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.customerAccountBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
