import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITaskCategory, getTaskCategoryIdentifier } from '../task-category.model';

export type EntityResponseType = HttpResponse<ITaskCategory>;
export type EntityArrayResponseType = HttpResponse<ITaskCategory[]>;

@Injectable({ providedIn: 'root' })
export class TaskCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/task-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(taskCategory: ITaskCategory): Observable<EntityResponseType> {
    return this.http.post<ITaskCategory>(this.resourceUrl, taskCategory, { observe: 'response' });
  }

  update(taskCategory: ITaskCategory): Observable<EntityResponseType> {
    return this.http.put<ITaskCategory>(`${this.resourceUrl}/${getTaskCategoryIdentifier(taskCategory) as number}`, taskCategory, {
      observe: 'response',
    });
  }

  partialUpdate(taskCategory: ITaskCategory): Observable<EntityResponseType> {
    return this.http.patch<ITaskCategory>(`${this.resourceUrl}/${getTaskCategoryIdentifier(taskCategory) as number}`, taskCategory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITaskCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaskCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTaskCategoryToCollectionIfMissing(
    taskCategoryCollection: ITaskCategory[],
    ...taskCategoriesToCheck: (ITaskCategory | null | undefined)[]
  ): ITaskCategory[] {
    const taskCategories: ITaskCategory[] = taskCategoriesToCheck.filter(isPresent);
    if (taskCategories.length > 0) {
      const taskCategoryCollectionIdentifiers = taskCategoryCollection.map(
        taskCategoryItem => getTaskCategoryIdentifier(taskCategoryItem)!
      );
      const taskCategoriesToAdd = taskCategories.filter(taskCategoryItem => {
        const taskCategoryIdentifier = getTaskCategoryIdentifier(taskCategoryItem);
        if (taskCategoryIdentifier == null || taskCategoryCollectionIdentifiers.includes(taskCategoryIdentifier)) {
          return false;
        }
        taskCategoryCollectionIdentifiers.push(taskCategoryIdentifier);
        return true;
      });
      return [...taskCategoriesToAdd, ...taskCategoryCollection];
    }
    return taskCategoryCollection;
  }
}
