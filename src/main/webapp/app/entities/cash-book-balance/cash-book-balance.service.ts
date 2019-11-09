import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashBookBalance } from 'app/shared/model/cash-book-balance.model';

type EntityResponseType = HttpResponse<ICashBookBalance>;
type EntityArrayResponseType = HttpResponse<ICashBookBalance[]>;

@Injectable({ providedIn: 'root' })
export class CashBookBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/cash-book-balances';

  constructor(protected http: HttpClient) {}

  create(cashBookBalance: ICashBookBalance): Observable<EntityResponseType> {
    return this.http.post<ICashBookBalance>(this.resourceUrl, cashBookBalance, { observe: 'response' });
  }

  update(cashBookBalance: ICashBookBalance): Observable<EntityResponseType> {
    return this.http.put<ICashBookBalance>(this.resourceUrl, cashBookBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICashBookBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICashBookBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
