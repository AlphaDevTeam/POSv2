import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { SalesAccountDetailComponent } from 'app/entities/sales-account/sales-account-detail.component';
import { SalesAccount } from 'app/shared/model/sales-account.model';

describe('Component Tests', () => {
  describe('SalesAccount Management Detail Component', () => {
    let comp: SalesAccountDetailComponent;
    let fixture: ComponentFixture<SalesAccountDetailComponent>;
    const route = ({ data: of({ salesAccount: new SalesAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SalesAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SalesAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SalesAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.salesAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
