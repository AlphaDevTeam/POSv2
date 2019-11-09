import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISupplierAccount } from 'app/shared/model/supplier-account.model';

type EntityResponseType = HttpResponse<ISupplierAccount>;
type EntityArrayResponseType = HttpResponse<ISupplierAccount[]>;

@Injectable({ providedIn: 'root' })
export class SupplierAccountService {
  public resourceUrl = SERVER_API_URL + 'api/supplier-accounts';

  constructor(protected http: HttpClient) {}

  create(supplierAccount: ISupplierAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(supplierAccount);
    return this.http
      .post<ISupplierAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(supplierAccount: ISupplierAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(supplierAccount);
    return this.http
      .put<ISupplierAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISupplierAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISupplierAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(supplierAccount: ISupplierAccount): ISupplierAccount {
    const copy: ISupplierAccount = Object.assign({}, supplierAccount, {
      transactionDate:
        supplierAccount.transactionDate != null && supplierAccount.transactionDate.isValid()
          ? supplierAccount.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((supplierAccount: ISupplierAccount) => {
        supplierAccount.transactionDate = supplierAccount.transactionDate != null ? moment(supplierAccount.transactionDate) : null;
      });
    }
    return res;
  }
}
