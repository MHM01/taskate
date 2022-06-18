import { ITasker } from 'app/entities/tasker/tasker.model';

export interface ITaskCategory {
  id?: number;
  name?: string | null;
  description?: string | null;
  taskers?: ITasker[] | null;
}

export class TaskCategory implements ITaskCategory {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public taskers?: ITasker[] | null) {}
}

export function getTaskCategoryIdentifier(taskCategory: ITaskCategory): number | undefined {
  return taskCategory.id;
}
