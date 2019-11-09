import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';

type EntityResponseType = HttpResponse<IPurchaseAccountBalance>;
type EntityArrayResponseType = HttpResponse<IPurchaseAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseAccountBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/purchase-account-balances';

  constructor(protected http: HttpClient) {}

  create(purchaseAccountBalance: IPurchaseAccountBalance): Observable<EntityResponseType> {
    return this.http.post<IPurchaseAccountBalance>(this.resourceUrl, purchaseAccountBalance, { observe: 'response' });
  }

  update(purchaseAccountBalance: IPurchaseAccountBalance): Observable<EntityResponseType> {
    return this.http.put<IPurchaseAccountBalance>(this.resourceUrl, purchaseAccountBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPurchaseAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPurchaseAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
