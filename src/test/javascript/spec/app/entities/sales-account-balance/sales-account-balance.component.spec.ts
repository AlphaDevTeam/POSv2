import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { SalesAccountBalanceComponent } from 'app/entities/sales-account-balance/sales-account-balance.component';
import { SalesAccountBalanceService } from 'app/entities/sales-account-balance/sales-account-balance.service';
import { SalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

describe('Component Tests', () => {
  describe('SalesAccountBalance Management Component', () => {
    let comp: SalesAccountBalanceComponent;
    let fixture: ComponentFixture<SalesAccountBalanceComponent>;
    let service: SalesAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SalesAccountBalanceComponent],
        providers: []
      })
        .overrideTemplate(SalesAccountBalanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SalesAccountBalanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SalesAccountBalanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SalesAccountBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.salesAccountBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
