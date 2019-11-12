import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDesigns } from 'app/shared/model/designs.model';

type EntityResponseType = HttpResponse<IDesigns>;
type EntityArrayResponseType = HttpResponse<IDesigns[]>;

@Injectable({ providedIn: 'root' })
export class DesignsService {
  public resourceUrl = SERVER_API_URL + 'api/designs';

  constructor(protected http: HttpClient) {}

  create(designs: IDesigns): Observable<EntityResponseType> {
    return this.http.post<IDesigns>(this.resourceUrl, designs, { observe: 'response' });
  }

  update(designs: IDesigns): Observable<EntityResponseType> {
    return this.http.put<IDesigns>(this.resourceUrl, designs, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDesigns>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDesigns[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
