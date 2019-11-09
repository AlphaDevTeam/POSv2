import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IJobStatus } from 'app/shared/model/job-status.model';

type EntityResponseType = HttpResponse<IJobStatus>;
type EntityArrayResponseType = HttpResponse<IJobStatus[]>;

@Injectable({ providedIn: 'root' })
export class JobStatusService {
  public resourceUrl = SERVER_API_URL + 'api/job-statuses';

  constructor(protected http: HttpClient) {}

  create(jobStatus: IJobStatus): Observable<EntityResponseType> {
    return this.http.post<IJobStatus>(this.resourceUrl, jobStatus, { observe: 'response' });
  }

  update(jobStatus: IJobStatus): Observable<EntityResponseType> {
    return this.http.put<IJobStatus>(this.resourceUrl, jobStatus, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
