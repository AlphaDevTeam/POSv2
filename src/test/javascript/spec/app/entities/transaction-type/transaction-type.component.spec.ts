import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PoSv2TestModule } from '../../../test.module';
import { TransactionTypeComponent } from 'app/entities/transaction-type/transaction-type.component';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { TransactionType } from 'app/shared/model/transaction-type.model';

describe('Component Tests', () => {
  describe('TransactionType Management Component', () => {
    let comp: TransactionTypeComponent;
    let fixture: ComponentFixture<TransactionTypeComponent>;
    let service: TransactionTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [TransactionTypeComponent],
        providers: []
      })
        .overrideTemplate(TransactionTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TransactionTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TransactionTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TransactionType(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.transactionTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
