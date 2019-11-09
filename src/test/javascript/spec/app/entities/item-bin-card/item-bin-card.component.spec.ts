import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { ItemBinCardComponent } from 'app/entities/item-bin-card/item-bin-card.component';
import { ItemBinCardService } from 'app/entities/item-bin-card/item-bin-card.service';
import { ItemBinCard } from 'app/shared/model/item-bin-card.model';

describe('Component Tests', () => {
  describe('ItemBinCard Management Component', () => {
    let comp: ItemBinCardComponent;
    let fixture: ComponentFixture<ItemBinCardComponent>;
    let service: ItemBinCardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ItemBinCardComponent],
        providers: []
      })
        .overrideTemplate(ItemBinCardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemBinCardComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ItemBinCardService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ItemBinCard(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.itemBinCards[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
