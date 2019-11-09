import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { DesingsComponent } from 'app/entities/desings/desings.component';
import { DesingsService } from 'app/entities/desings/desings.service';
import { Desings } from 'app/shared/model/desings.model';

describe('Component Tests', () => {
  describe('Desings Management Component', () => {
    let comp: DesingsComponent;
    let fixture: ComponentFixture<DesingsComponent>;
    let service: DesingsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DesingsComponent],
        providers: []
      })
        .overrideTemplate(DesingsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DesingsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DesingsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Desings(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.desings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
