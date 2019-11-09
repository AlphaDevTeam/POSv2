import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';

type EntityResponseType = HttpResponse<ICustomerAccountBalance>;
type EntityArrayResponseType = HttpResponse<ICustomerAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class CustomerAccountBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/customer-account-balances';

  constructor(protected http: HttpClient) {}

  create(customerAccountBalance: ICustomerAccountBalance): Observable<EntityResponseType> {
    return this.http.post<ICustomerAccountBalance>(this.resourceUrl, customerAccountBalance, { observe: 'response' });
  }

  update(customerAccountBalance: ICustomerAccountBalance): Observable<EntityResponseType> {
    return this.http.put<ICustomerAccountBalance>(this.resourceUrl, customerAccountBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomerAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
