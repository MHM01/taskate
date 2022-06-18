import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';
import { ITask } from 'app/entities/task/task.model';
import { LicenseStatus } from 'app/entities/enumerations/license-status.model';

export interface IClient {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  subscribedAt?: dayjs.Dayjs | null;
  licenseStatus?: LicenseStatus | null;
  address?: ILocation | null;
  tasks?: ITask | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public subscribedAt?: dayjs.Dayjs | null,
    public licenseStatus?: LicenseStatus | null,
    public address?: ILocation | null,
    public tasks?: ITask | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
