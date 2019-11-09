import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { ItemsService } from 'app/entities/items/items.service';
import { IItems, Items } from 'app/shared/model/items.model';

describe('Service Tests', () => {
  describe('Items Service', () => {
    let injector: TestBed;
    let service: ItemsService;
    let httpMock: HttpTestingController;
    let elemDefault: IItems;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ItemsService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Items(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA', 0, 0, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            originalStockDate: currentDate.format(DATE_FORMAT),
            modifiedStockDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Items', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            originalStockDate: currentDate.format(DATE_FORMAT),
            modifiedStockDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            originalStockDate: currentDate,
            modifiedStockDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Items(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Items', () => {
        const returnedFromService = Object.assign(
          {
            itemCode: 'BBBBBB',
            itemName: 'BBBBBB',
            itemDescription: 'BBBBBB',
            itemPrice: 1,
            itemSerial: 'BBBBBB',
            itemSupplierSerial: 'BBBBBB',
            itemCost: 1,
            itemSalePrice: 1,
            originalStockDate: currentDate.format(DATE_FORMAT),
            modifiedStockDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            originalStockDate: currentDate,
            modifiedStockDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Items', () => {
        const returnedFromService = Object.assign(
          {
            itemCode: 'BBBBBB',
            itemName: 'BBBBBB',
            itemDescription: 'BBBBBB',
            itemPrice: 1,
            itemSerial: 'BBBBBB',
            itemSupplierSerial: 'BBBBBB',
            itemCost: 1,
            itemSalePrice: 1,
            originalStockDate: currentDate.format(DATE_FORMAT),
            modifiedStockDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            originalStockDate: currentDate,
            modifiedStockDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Items', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
