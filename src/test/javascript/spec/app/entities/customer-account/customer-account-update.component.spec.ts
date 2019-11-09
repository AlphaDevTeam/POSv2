import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { CustomerAccountUpdateComponent } from 'app/entities/customer-account/customer-account-update.component';
import { CustomerAccountService } from 'app/entities/customer-account/customer-account.service';
import { CustomerAccount } from 'app/shared/model/customer-account.model';

describe('Component Tests', () => {
  describe('CustomerAccount Management Update Component', () => {
    let comp: CustomerAccountUpdateComponent;
    let fixture: ComponentFixture<CustomerAccountUpdateComponent>;
    let service: CustomerAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CustomerAccountUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CustomerAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CustomerAccount(123);
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
        const entity = new CustomerAccount();
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
