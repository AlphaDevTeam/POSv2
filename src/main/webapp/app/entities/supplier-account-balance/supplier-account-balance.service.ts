import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';

type EntityResponseType = HttpResponse<ISupplierAccountBalance>;
type EntityArrayResponseType = HttpResponse<ISupplierAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class SupplierAccountBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/supplier-account-balances';

  constructor(protected http: HttpClient) {}

  create(supplierAccountBalance: ISupplierAccountBalance): Observable<EntityResponseType> {
    return this.http.post<ISupplierAccountBalance>(this.resourceUrl, supplierAccountBalance, { observe: 'response' });
  }

  update(supplierAccountBalance: ISupplierAccountBalance): Observable<EntityResponseType> {
    return this.http.put<ISupplierAccountBalance>(this.resourceUrl, supplierAccountBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISupplierAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISupplierAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
