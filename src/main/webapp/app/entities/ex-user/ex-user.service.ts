import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExUser } from 'app/shared/model/ex-user.model';

type EntityResponseType = HttpResponse<IExUser>;
type EntityArrayResponseType = HttpResponse<IExUser[]>;

@Injectable({ providedIn: 'root' })
export class ExUserService {
  public resourceUrl = SERVER_API_URL + 'api/ex-users';

  constructor(protected http: HttpClient) {}

  create(exUser: IExUser): Observable<EntityResponseType> {
    return this.http.post<IExUser>(this.resourceUrl, exUser, { observe: 'response' });
  }

  update(exUser: IExUser): Observable<EntityResponseType> {
    return this.http.put<IExUser>(this.resourceUrl, exUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
