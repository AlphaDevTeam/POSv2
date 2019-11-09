import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

type EntityResponseType = HttpResponse<IGoodsReceiptDetails>;
type EntityArrayResponseType = HttpResponse<IGoodsReceiptDetails[]>;

@Injectable({ providedIn: 'root' })
export class GoodsReceiptDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/goods-receipt-details';

  constructor(protected http: HttpClient) {}

  create(goodsReceiptDetails: IGoodsReceiptDetails): Observable<EntityResponseType> {
    return this.http.post<IGoodsReceiptDetails>(this.resourceUrl, goodsReceiptDetails, { observe: 'response' });
  }

  update(goodsReceiptDetails: IGoodsReceiptDetails): Observable<EntityResponseType> {
    return this.http.put<IGoodsReceiptDetails>(this.resourceUrl, goodsReceiptDetails, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGoodsReceiptDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGoodsReceiptDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
