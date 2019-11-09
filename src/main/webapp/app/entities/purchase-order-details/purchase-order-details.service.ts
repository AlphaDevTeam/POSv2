import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';

type EntityResponseType = HttpResponse<IPurchaseOrderDetails>;
type EntityArrayResponseType = HttpResponse<IPurchaseOrderDetails[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/purchase-order-details';

  constructor(protected http: HttpClient) {}

  create(purchaseOrderDetails: IPurchaseOrderDetails): Observable<EntityResponseType> {
    return this.http.post<IPurchaseOrderDetails>(this.resourceUrl, purchaseOrderDetails, { observe: 'response' });
  }

  update(purchaseOrderDetails: IPurchaseOrderDetails): Observable<EntityResponseType> {
    return this.http.put<IPurchaseOrderDetails>(this.resourceUrl, purchaseOrderDetails, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPurchaseOrderDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPurchaseOrderDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
