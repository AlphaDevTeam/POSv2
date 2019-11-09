import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { ItemsComponent } from 'app/entities/items/items.component';
import { ItemsService } from 'app/entities/items/items.service';
import { Items } from 'app/shared/model/items.model';

describe('Component Tests', () => {
  describe('Items Management Component', () => {
    let comp: ItemsComponent;
    let fixture: ComponentFixture<ItemsComponent>;
    let service: ItemsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ItemsComponent],
        providers: []
      })
        .overrideTemplate(ItemsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ItemsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Items(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.items[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
