import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaskCategoryComponent } from '../list/task-category.component';
import { TaskCategoryDetailComponent } from '../detail/task-category-detail.component';
import { TaskCategoryUpdateComponent } from '../update/task-category-update.component';
import { TaskCategoryRoutingResolveService } from './task-category-routing-resolve.service';

const taskCategoryRoute: Routes = [
  {
    path: '',
    component: TaskCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaskCategoryDetailComponent,
    resolve: {
      taskCategory: TaskCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaskCategoryUpdateComponent,
    resolve: {
      taskCategory: TaskCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaskCategoryUpdateComponent,
    resolve: {
      taskCategory: TaskCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(taskCategoryRoute)],
  exports: [RouterModule],
})
export class TaskCategoryRoutingModule {}
