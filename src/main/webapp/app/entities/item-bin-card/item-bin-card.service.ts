import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IItemBinCard } from 'app/shared/model/item-bin-card.model';

type EntityResponseType = HttpResponse<IItemBinCard>;
type EntityArrayResponseType = HttpResponse<IItemBinCard[]>;

@Injectable({ providedIn: 'root' })
export class ItemBinCardService {
  public resourceUrl = SERVER_API_URL + 'api/item-bin-cards';

  constructor(protected http: HttpClient) {}

  create(itemBinCard: IItemBinCard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemBinCard);
    return this.http
      .post<IItemBinCard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemBinCard: IItemBinCard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemBinCard);
    return this.http
      .put<IItemBinCard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemBinCard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemBinCard[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(itemBinCard: IItemBinCard): IItemBinCard {
    const copy: IItemBinCard = Object.assign({}, itemBinCard, {
      transactionDate:
        itemBinCard.transactionDate != null && itemBinCard.transactionDate.isValid()
          ? itemBinCard.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((itemBinCard: IItemBinCard) => {
        itemBinCard.transactionDate = itemBinCard.transactionDate != null ? moment(itemBinCard.transactionDate) : null;
      });
    }
    return res;
  }
}
