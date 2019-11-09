import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseAccountDetailComponent } from 'app/entities/purchase-account/purchase-account-detail.component';
import { PurchaseAccount } from 'app/shared/model/purchase-account.model';

describe('Component Tests', () => {
  describe('PurchaseAccount Management Detail Component', () => {
    let comp: PurchaseAccountDetailComponent;
    let fixture: ComponentFixture<PurchaseAccountDetailComponent>;
    const route = ({ data: of({ purchaseAccount: new PurchaseAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PurchaseAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaseAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.purchaseAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
