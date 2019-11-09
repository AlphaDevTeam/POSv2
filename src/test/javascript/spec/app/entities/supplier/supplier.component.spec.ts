import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { SupplierComponent } from 'app/entities/supplier/supplier.component';
import { SupplierService } from 'app/entities/supplier/supplier.service';
import { Supplier } from 'app/shared/model/supplier.model';

describe('Component Tests', () => {
  describe('Supplier Management Component', () => {
    let comp: SupplierComponent;
    let fixture: ComponentFixture<SupplierComponent>;
    let service: SupplierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [SupplierComponent],
        providers: []
      })
        .overrideTemplate(SupplierComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SupplierComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SupplierService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Supplier(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.suppliers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
