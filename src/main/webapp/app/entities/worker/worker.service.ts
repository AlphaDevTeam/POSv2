import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IWorker } from 'app/shared/model/worker.model';

type EntityResponseType = HttpResponse<IWorker>;
type EntityArrayResponseType = HttpResponse<IWorker[]>;

@Injectable({ providedIn: 'root' })
export class WorkerService {
  public resourceUrl = SERVER_API_URL + 'api/workers';

  constructor(protected http: HttpClient) {}

  create(worker: IWorker): Observable<EntityResponseType> {
    return this.http.post<IWorker>(this.resourceUrl, worker, { observe: 'response' });
  }

  update(worker: IWorker): Observable<EntityResponseType> {
    return this.http.put<IWorker>(this.resourceUrl, worker, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorker>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorker[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
