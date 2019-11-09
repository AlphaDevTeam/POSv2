import { IItems } from 'app/shared/model/items.model';
import { IJob } from 'app/shared/model/job.model';

export interface IJobDetais {
  id?: number;
  jobItemPrice?: number;
  jobItemQty?: number;
  item?: IItems;
  job?: IJob;
}

export class JobDetais implements IJobDetais {
  constructor(public id?: number, public jobItemPrice?: number, public jobItemQty?: number, public item?: IItems, public job?: IJob) {}
}
