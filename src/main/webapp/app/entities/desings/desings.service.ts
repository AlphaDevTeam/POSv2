import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDesings } from 'app/shared/model/desings.model';

type EntityResponseType = HttpResponse<IDesings>;
type EntityArrayResponseType = HttpResponse<IDesings[]>;

@Injectable({ providedIn: 'root' })
export class DesingsService {
  public resourceUrl = SERVER_API_URL + 'api/desings';

  constructor(protected http: HttpClient) {}

  create(desings: IDesings): Observable<EntityResponseType> {
    return this.http.post<IDesings>(this.resourceUrl, desings, { observe: 'response' });
  }

  update(desings: IDesings): Observable<EntityResponseType> {
    return this.http.put<IDesings>(this.resourceUrl, desings, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDesings>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDesings[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
