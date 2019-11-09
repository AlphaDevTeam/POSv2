import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { PurchaseAccountBalanceUpdateComponent } from 'app/entities/purchase-account-balance/purchase-account-balance-update.component';
import { PurchaseAccountBalanceService } from 'app/entities/purchase-account-balance/purchase-account-balance.service';
import { PurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';

describe('Component Tests', () => {
  describe('PurchaseAccountBalance Management Update Component', () => {
    let comp: PurchaseAccountBalanceUpdateComponent;
    let fixture: ComponentFixture<PurchaseAccountBalanceUpdateComponent>;
    let service: PurchaseAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [PurchaseAccountBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PurchaseAccountBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaseAccountBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseAccountBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PurchaseAccountBalance(123);
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
        const entity = new PurchaseAccountBalance();
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
