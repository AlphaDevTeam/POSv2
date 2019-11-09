import { ILocation } from 'app/shared/model/location.model';
import { IJob } from 'app/shared/model/job.model';

export interface IWorker {
  id?: number;
  workerCode?: string;
  workerName?: string;
  workerLimit?: number;
  isActive?: boolean;
  location?: ILocation;
  jobs?: IJob;
}

export class Worker implements IWorker {
  constructor(
    public id?: number,
    public workerCode?: string,
    public workerName?: string,
    public workerLimit?: number,
    public isActive?: boolean,
    public location?: ILocation,
    public jobs?: IJob
  ) {
    this.isActive = this.isActive || false;
  }
}
