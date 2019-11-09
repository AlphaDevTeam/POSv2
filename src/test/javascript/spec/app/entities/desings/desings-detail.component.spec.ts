import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { DesingsDetailComponent } from 'app/entities/desings/desings-detail.component';
import { Desings } from 'app/shared/model/desings.model';

describe('Component Tests', () => {
  describe('Desings Management Detail Component', () => {
    let comp: DesingsDetailComponent;
    let fixture: ComponentFixture<DesingsDetailComponent>;
    const route = ({ data: of({ desings: new Desings(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DesingsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DesingsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DesingsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.desings).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
