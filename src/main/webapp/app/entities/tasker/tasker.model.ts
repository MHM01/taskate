import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';
import { ITask } from 'app/entities/task/task.model';
import { ITaskCategory } from 'app/entities/task-category/task-category.model';
import { LicenseStatus } from 'app/entities/enumerations/license-status.model';

export interface ITasker {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  subscribedAt?: dayjs.Dayjs | null;
  licenseStatus?: LicenseStatus | null;
  address?: ILocation | null;
  tasks?: ITask[] | null;
  taskCategories?: ITaskCategory | null;
}

export class Tasker implements ITasker {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public subscribedAt?: dayjs.Dayjs | null,
    public licenseStatus?: LicenseStatus | null,
    public address?: ILocation | null,
    public tasks?: ITask[] | null,
    public taskCategories?: ITaskCategory | null
  ) {}
}

export function getTaskerIdentifier(tasker: ITasker): number | undefined {
  return tasker.id;
}
