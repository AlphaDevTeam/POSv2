import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { DesignsComponent } from 'app/entities/designs/designs.component';
import { DesignsService } from 'app/entities/designs/designs.service';
import { Designs } from 'app/shared/model/designs.model';

describe('Component Tests', () => {
  describe('Designs Management Component', () => {
    let comp: DesignsComponent;
    let fixture: ComponentFixture<DesignsComponent>;
    let service: DesignsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [DesignsComponent],
        providers: []
      })
        .overrideTemplate(DesignsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DesignsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DesignsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Designs(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.designs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
