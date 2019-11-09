import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { CashBookComponent } from 'app/entities/cash-book/cash-book.component';
import { CashBookService } from 'app/entities/cash-book/cash-book.service';
import { CashBook } from 'app/shared/model/cash-book.model';

describe('Component Tests', () => {
  describe('CashBook Management Component', () => {
    let comp: CashBookComponent;
    let fixture: ComponentFixture<CashBookComponent>;
    let service: CashBookService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [CashBookComponent],
        providers: []
      })
        .overrideTemplate(CashBookComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashBookComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashBookService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashBook(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashBooks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
