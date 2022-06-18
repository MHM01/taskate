import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaskCategory, TaskCategory } from '../task-category.model';
import { TaskCategoryService } from '../service/task-category.service';

@Injectable({ providedIn: 'root' })
export class TaskCategoryRoutingResolveService implements Resolve<ITaskCategory> {
  constructor(protected service: TaskCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaskCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((taskCategory: HttpResponse<TaskCategory>) => {
          if (taskCategory.body) {
            return of(taskCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaskCategory());
  }
}
