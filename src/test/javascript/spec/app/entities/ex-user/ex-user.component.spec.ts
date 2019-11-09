import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { ExUserComponent } from 'app/entities/ex-user/ex-user.component';
import { ExUserService } from 'app/entities/ex-user/ex-user.service';
import { ExUser } from 'app/shared/model/ex-user.model';

describe('Component Tests', () => {
  describe('ExUser Management Component', () => {
    let comp: ExUserComponent;
    let fixture: ComponentFixture<ExUserComponent>;
    let service: ExUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ExUserComponent],
        providers: []
      })
        .overrideTemplate(ExUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ExUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.exUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
