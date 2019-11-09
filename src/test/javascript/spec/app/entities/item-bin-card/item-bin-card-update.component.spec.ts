import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { ItemBinCardUpdateComponent } from 'app/entities/item-bin-card/item-bin-card-update.component';
import { ItemBinCardService } from 'app/entities/item-bin-card/item-bin-card.service';
import { ItemBinCard } from 'app/shared/model/item-bin-card.model';

describe('Component Tests', () => {
  describe('ItemBinCard Management Update Component', () => {
    let comp: ItemBinCardUpdateComponent;
    let fixture: ComponentFixture<ItemBinCardUpdateComponent>;
    let service: ItemBinCardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ItemBinCardUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ItemBinCardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemBinCardUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ItemBinCardService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ItemBinCard(123);
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
        const entity = new ItemBinCard();
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
