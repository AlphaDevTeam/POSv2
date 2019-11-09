import { ILocation } from 'app/shared/model/location.model';

export interface IJobStatus {
  id?: number;
  jobStatusCode?: string;
  jobStatusDescription?: string;
  isActive?: boolean;
  location?: ILocation;
}

export class JobStatus implements IJobStatus {
  constructor(
    public id?: number,
    public jobStatusCode?: string,
    public jobStatusDescription?: string,
    public isActive?: boolean,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}
