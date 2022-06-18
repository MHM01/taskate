import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITasker, getTaskerIdentifier } from '../tasker.model';

export type EntityResponseType = HttpResponse<ITasker>;
export type EntityArrayResponseType = HttpResponse<ITasker[]>;

@Injectable({ providedIn: 'root' })
export class TaskerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/taskers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tasker: ITasker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tasker);
    return this.http
      .post<ITasker>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tasker: ITasker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tasker);
    return this.http
      .put<ITasker>(`${this.resourceUrl}/${getTaskerIdentifier(tasker) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tasker: ITasker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tasker);
    return this.http
      .patch<ITasker>(`${this.resourceUrl}/${getTaskerIdentifier(tasker) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITasker>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITasker[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTaskerToCollectionIfMissing(taskerCollection: ITasker[], ...taskersToCheck: (ITasker | null | undefined)[]): ITasker[] {
    const taskers: ITasker[] = taskersToCheck.filter(isPresent);
    if (taskers.length > 0) {
      const taskerCollectionIdentifiers = taskerCollection.map(taskerItem => getTaskerIdentifier(taskerItem)!);
      const taskersToAdd = taskers.filter(taskerItem => {
        const taskerIdentifier = getTaskerIdentifier(taskerItem);
        if (taskerIdentifier == null || taskerCollectionIdentifiers.includes(taskerIdentifier)) {
          return false;
        }
        taskerCollectionIdentifiers.push(taskerIdentifier);
        return true;
      });
      return [...taskersToAdd, ...taskerCollection];
    }
    return taskerCollection;
  }

  protected convertDateFromClient(tasker: ITasker): ITasker {
    return Object.assign({}, tasker, {
      subscribedAt: tasker.subscribedAt?.isValid() ? tasker.subscribedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.subscribedAt = res.body.subscribedAt ? dayjs(res.body.subscribedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tasker: ITasker) => {
        tasker.subscribedAt = tasker.subscribedAt ? dayjs(tasker.subscribedAt) : undefined;
      });
    }
    return res;
  }
}
