import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { CashBookBalanceComponent } from 'app/entities/cash-book-balance/cash-book-balance.component';
import { CashBookBalanceService } from 'app/entities/cash-book-balance/cash-book-balance.service';
import { CashBookBalance } from 'app/shared/model/cash-book-balance.model';

describe('Component Tests', () => {
  describe('CashBookBalance Management Component', () => {
    let comp: CashBookBalanceComponent;
    let fixture: ComponentFixture<CashBookBalanceComponent>;
    let service: CashBookBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CashBookBalanceComponent],
        providers: []
      })
        .overrideTemplate(CashBookBalanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashBookBalanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashBookBalanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashBookBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashBookBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
