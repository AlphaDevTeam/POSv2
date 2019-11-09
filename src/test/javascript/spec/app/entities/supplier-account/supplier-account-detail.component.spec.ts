import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { SupplierAccountDetailComponent } from 'app/entities/supplier-account/supplier-account-detail.component';
import { SupplierAccount } from 'app/shared/model/supplier-account.model';

describe('Component Tests', () => {
  describe('SupplierAccount Management Detail Component', () => {
    let comp: SupplierAccountDetailComponent;
    let fixture: ComponentFixture<SupplierAccountDetailComponent>;
    const route = ({ data: of({ supplierAccount: new SupplierAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SupplierAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SupplierAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SupplierAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.supplierAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
