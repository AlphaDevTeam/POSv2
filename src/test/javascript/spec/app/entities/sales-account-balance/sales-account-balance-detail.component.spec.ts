import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { SalesAccountBalanceDetailComponent } from 'app/entities/sales-account-balance/sales-account-balance-detail.component';
import { SalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

describe('Component Tests', () => {
  describe('SalesAccountBalance Management Detail Component', () => {
    let comp: SalesAccountBalanceDetailComponent;
    let fixture: ComponentFixture<SalesAccountBalanceDetailComponent>;
    const route = ({ data: of({ salesAccountBalance: new SalesAccountBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SalesAccountBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SalesAccountBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SalesAccountBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.salesAccountBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
