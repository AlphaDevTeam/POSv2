import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

type EntityResponseType = HttpResponse<ISalesAccountBalance>;
type EntityArrayResponseType = HttpResponse<ISalesAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class SalesAccountBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/sales-account-balances';

  constructor(protected http: HttpClient) {}

  create(salesAccountBalance: ISalesAccountBalance): Observable<EntityResponseType> {
    return this.http.post<ISalesAccountBalance>(this.resourceUrl, salesAccountBalance, { observe: 'response' });
  }

  update(salesAccountBalance: ISalesAccountBalance): Observable<EntityResponseType> {
    return this.http.put<ISalesAccountBalance>(this.resourceUrl, salesAccountBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISalesAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalesAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
