import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';

type EntityResponseType = HttpResponse<IGoodsReceipt>;
type EntityArrayResponseType = HttpResponse<IGoodsReceipt[]>;

@Injectable({ providedIn: 'root' })
export class GoodsReceiptService {
  public resourceUrl = SERVER_API_URL + 'api/goods-receipts';

  constructor(protected http: HttpClient) {}

  create(goodsReceipt: IGoodsReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(goodsReceipt);
    return this.http
      .post<IGoodsReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(goodsReceipt: IGoodsReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(goodsReceipt);
    return this.http
      .put<IGoodsReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGoodsReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGoodsReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(goodsReceipt: IGoodsReceipt): IGoodsReceipt {
    const copy: IGoodsReceipt = Object.assign({}, goodsReceipt, {
      grnDate: goodsReceipt.grnDate != null && goodsReceipt.grnDate.isValid() ? goodsReceipt.grnDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.grnDate = res.body.grnDate != null ? moment(res.body.grnDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((goodsReceipt: IGoodsReceipt) => {
        goodsReceipt.grnDate = goodsReceipt.grnDate != null ? moment(goodsReceipt.grnDate) : null;
      });
    }
    return res;
  }
}
