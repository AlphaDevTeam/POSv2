import { Moment } from 'moment';
import { IJobStatus } from 'app/shared/model/job-status.model';
import { IJobDetais } from 'app/shared/model/job-detais.model';
import { IWorker } from 'app/shared/model/worker.model';
import { ILocation } from 'app/shared/model/location.model';
import { ICustomer } from 'app/shared/model/customer.model';

export interface IJob {
  id?: number;
  jobCode?: string;
  jobDescription?: string;
  jobStartDate?: Moment;
  jobEndDate?: Moment;
  jobAmount?: number;
  status?: IJobStatus;
  details?: IJobDetais[];
  assignedTos?: IWorker[];
  location?: ILocation;
  customer?: ICustomer;
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public jobCode?: string,
    public jobDescription?: string,
    public jobStartDate?: Moment,
    public jobEndDate?: Moment,
    public jobAmount?: number,
    public status?: IJobStatus,
    public details?: IJobDetais[],
    public assignedTos?: IWorker[],
    public location?: ILocation,
    public customer?: ICustomer
  ) {}
}
