import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { DesignsDetailComponent } from 'app/entities/designs/designs-detail.component';
import { Designs } from 'app/shared/model/designs.model';

describe('Component Tests', () => {
  describe('Designs Management Detail Component', () => {
    let comp: DesignsDetailComponent;
    let fixture: ComponentFixture<DesignsDetailComponent>;
    const route = ({ data: of({ designs: new Designs(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DesignsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DesignsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DesignsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.designs).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
