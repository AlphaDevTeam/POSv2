import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { CashBookBalanceUpdateComponent } from 'app/entities/cash-book-balance/cash-book-balance-update.component';
import { CashBookBalanceService } from 'app/entities/cash-book-balance/cash-book-balance.service';
import { CashBookBalance } from 'app/shared/model/cash-book-balance.model';

describe('Component Tests', () => {
  describe('CashBookBalance Management Update Component', () => {
    let comp: CashBookBalanceUpdateComponent;
    let fixture: ComponentFixture<CashBookBalanceUpdateComponent>;
    let service: CashBookBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CashBookBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashBookBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashBookBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashBookBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashBookBalance(123);
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
        const entity = new CashBookBalance();
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
