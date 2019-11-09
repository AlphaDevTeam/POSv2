import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { CustomerAccountComponent } from 'app/entities/customer-account/customer-account.component';
import { CustomerAccountService } from 'app/entities/customer-account/customer-account.service';
import { CustomerAccount } from 'app/shared/model/customer-account.model';

describe('Component Tests', () => {
  describe('CustomerAccount Management Component', () => {
    let comp: CustomerAccountComponent;
    let fixture: ComponentFixture<CustomerAccountComponent>;
    let service: CustomerAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CustomerAccountComponent],
        providers: []
      })
        .overrideTemplate(CustomerAccountComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerAccountComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerAccountService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CustomerAccount(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.customerAccounts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
