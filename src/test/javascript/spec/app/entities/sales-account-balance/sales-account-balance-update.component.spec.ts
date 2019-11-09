import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { SalesAccountBalanceUpdateComponent } from 'app/entities/sales-account-balance/sales-account-balance-update.component';
import { SalesAccountBalanceService } from 'app/entities/sales-account-balance/sales-account-balance.service';
import { SalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

describe('Component Tests', () => {
  describe('SalesAccountBalance Management Update Component', () => {
    let comp: SalesAccountBalanceUpdateComponent;
    let fixture: ComponentFixture<SalesAccountBalanceUpdateComponent>;
    let service: SalesAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SalesAccountBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SalesAccountBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SalesAccountBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SalesAccountBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SalesAccountBalance(123);
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
        const entity = new SalesAccountBalance();
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
