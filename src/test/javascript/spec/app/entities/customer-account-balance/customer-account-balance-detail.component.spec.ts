import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { CustomerAccountBalanceDetailComponent } from 'app/entities/customer-account-balance/customer-account-balance-detail.component';
import { CustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';

describe('Component Tests', () => {
  describe('CustomerAccountBalance Management Detail Component', () => {
    let comp: CustomerAccountBalanceDetailComponent;
    let fixture: ComponentFixture<CustomerAccountBalanceDetailComponent>;
    const route = ({ data: of({ customerAccountBalance: new CustomerAccountBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CustomerAccountBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CustomerAccountBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomerAccountBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.customerAccountBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
