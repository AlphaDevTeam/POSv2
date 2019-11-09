import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoSv2TestModule } from '../../../test.module';
import { ExUserDetailComponent } from 'app/entities/ex-user/ex-user-detail.component';
import { ExUser } from 'app/shared/model/ex-user.model';

describe('Component Tests', () => {
  describe('ExUser Management Detail Component', () => {
    let comp: ExUserDetailComponent;
    let fixture: ComponentFixture<ExUserDetailComponent>;
    const route = ({ data: of({ exUser: new ExUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ExUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.exUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
