import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { CustomerAccountBalanceUpdateComponent } from 'app/entities/customer-account-balance/customer-account-balance-update.component';
import { CustomerAccountBalanceService } from 'app/entities/customer-account-balance/customer-account-balance.service';
import { CustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';

describe('Component Tests', () => {
  describe('CustomerAccountBalance Management Update Component', () => {
    let comp: CustomerAccountBalanceUpdateComponent;
    let fixture: ComponentFixture<CustomerAccountBalanceUpdateComponent>;
    let service: CustomerAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CustomerAccountBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CustomerAccountBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerAccountBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerAccountBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CustomerAccountBalance(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CustomerAccountBalance();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
