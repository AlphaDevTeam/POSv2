import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICustomerAccount } from 'app/shared/model/customer-account.model';

type EntityResponseType = HttpResponse<ICustomerAccount>;
type EntityArrayResponseType = HttpResponse<ICustomerAccount[]>;

@Injectable({ providedIn: 'root' })
export class CustomerAccountService {
  public resourceUrl = SERVER_API_URL + 'api/customer-accounts';

  constructor(protected http: HttpClient) {}

  create(customerAccount: ICustomerAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerAccount);
    return this.http
      .post<ICustomerAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(customerAccount: ICustomerAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerAccount);
    return this.http
      .put<ICustomerAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICustomerAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICustomerAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(customerAccount: ICustomerAccount): ICustomerAccount {
    const copy: ICustomerAccount = Object.assign({}, customerAccount, {
      transactionDate:
        customerAccount.transactionDate != null && customerAccount.transactionDate.isValid()
          ? customerAccount.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((customerAccount: ICustomerAccount) => {
        customerAccount.transactionDate = customerAccount.transactionDate != null ? moment(customerAccount.transactionDate) : null;
      });
    }
    return res;
  }
}
