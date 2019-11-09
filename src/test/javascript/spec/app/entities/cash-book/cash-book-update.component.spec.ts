import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { CashBookUpdateComponent } from 'app/entities/cash-book/cash-book-update.component';
import { CashBookService } from 'app/entities/cash-book/cash-book.service';
import { CashBook } from 'app/shared/model/cash-book.model';

describe('Component Tests', () => {
  describe('CashBook Management Update Component', () => {
    let comp: CashBookUpdateComponent;
    let fixture: ComponentFixture<CashBookUpdateComponent>;
    let service: CashBookService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CashBookUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashBookUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashBookUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashBookService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashBook(123);
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
        const entity = new CashBook();
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
