import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { ItemsDetailComponent } from 'app/entities/items/items-detail.component';
import { Items } from 'app/shared/model/items.model';

describe('Component Tests', () => {
  describe('Items Management Detail Component', () => {
    let comp: ItemsDetailComponent;
    let fixture: ComponentFixture<ItemsDetailComponent>;
    const route = ({ data: of({ items: new Items(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ItemsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ItemsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.items).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
