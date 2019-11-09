import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { GoodsReceiptService } from 'app/entities/goods-receipt/goods-receipt.service';
import { IGoodsReceipt, GoodsReceipt } from 'app/shared/model/goods-receipt.model';

describe('Service Tests', () => {
  describe('GoodsReceipt Service', () => {
    let injector: TestBed;
    let service: GoodsReceiptService;
    let httpMock: HttpTestingController;
    let elemDefault: IGoodsReceipt;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(GoodsReceiptService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new GoodsReceipt(0, 'AAAAAAA', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            grnDate: currentDate.format(DATE_FORMAT)
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

      it('should create a GoodsReceipt', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            grnDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            grnDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new GoodsReceipt(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a GoodsReceipt', () => {
        const returnedFromService = Object.assign(
          {
            grnNumber: 'BBBBBB',
            grnDate: currentDate.format(DATE_FORMAT),
            poNumber: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            grnDate: currentDate
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

      it('should return a list of GoodsReceipt', () => {
        const returnedFromService = Object.assign(
          {
            grnNumber: 'BBBBBB',
            grnDate: currentDate.format(DATE_FORMAT),
            poNumber: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            grnDate: currentDate
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

      it('should delete a GoodsReceipt', () => {
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
