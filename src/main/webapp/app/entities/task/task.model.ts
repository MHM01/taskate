import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';
import { ITaskCategory } from 'app/entities/task-category/task-category.model';
import { IClient } from 'app/entities/client/client.model';
import { ITasker } from 'app/entities/tasker/tasker.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';

export interface ITask {
  id?: number;
  title?: string | null;
  description?: string | null;
  numberOfTaskerRequired?: number | null;
  budget?: number | null;
  startDate?: dayjs.Dayjs | null;
  status?: TaskStatus | null;
  location?: ILocation | null;
  taskCategory?: ITaskCategory | null;
  clients?: IClient[] | null;
  taskers?: ITasker | null;
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public numberOfTaskerRequired?: number | null,
    public budget?: number | null,
    public startDate?: dayjs.Dayjs | null,
    public status?: TaskStatus | null,
    public location?: ILocation | null,
    public taskCategory?: ITaskCategory | null,
    public clients?: IClient[] | null,
    public taskers?: ITasker | null
  ) {}
}

export function getTaskIdentifier(task: ITask): number | undefined {
  return task.id;
}
