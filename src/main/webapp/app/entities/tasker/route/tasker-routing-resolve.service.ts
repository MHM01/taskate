import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITasker, Tasker } from '../tasker.model';
import { TaskerService } from '../service/tasker.service';

@Injectable({ providedIn: 'root' })
export class TaskerRoutingResolveService implements Resolve<ITasker> {
  constructor(protected service: TaskerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITasker> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tasker: HttpResponse<Tasker>) => {
          if (tasker.body) {
            return of(tasker.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tasker());
  }
}
