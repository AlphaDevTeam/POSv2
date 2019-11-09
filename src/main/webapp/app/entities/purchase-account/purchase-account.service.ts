import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPurchaseAccount } from 'app/shared/model/purchase-account.model';

type EntityResponseType = HttpResponse<IPurchaseAccount>;
type EntityArrayResponseType = HttpResponse<IPurchaseAccount[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseAccountService {
  public resourceUrl = SERVER_API_URL + 'api/purchase-accounts';

  constructor(protected http: HttpClient) {}

  create(purchaseAccount: IPurchaseAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseAccount);
    return this.http
      .post<IPurchaseAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchaseAccount: IPurchaseAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseAccount);
    return this.http
      .put<IPurchaseAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchaseAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(purchaseAccount: IPurchaseAccount): IPurchaseAccount {
    const copy: IPurchaseAccount = Object.assign({}, purchaseAccount, {
      transactionDate:
        purchaseAccount.transactionDate != null && purchaseAccount.transactionDate.isValid()
          ? purchaseAccount.transactionDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transactionDate = res.body.transactionDate != null ? moment(res.body.transactionDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((purchaseAccount: IPurchaseAccount) => {
        purchaseAccount.transactionDate = purchaseAccount.transactionDate != null ? moment(purchaseAccount.transactionDate) : null;
      });
    }
    return res;
  }
}
