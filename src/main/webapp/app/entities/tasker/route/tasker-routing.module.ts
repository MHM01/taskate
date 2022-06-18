import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaskerComponent } from '../list/tasker.component';
import { TaskerDetailComponent } from '../detail/tasker-detail.component';
import { TaskerUpdateComponent } from '../update/tasker-update.component';
import { TaskerRoutingResolveService } from './tasker-routing-resolve.service';

const taskerRoute: Routes = [
  {
    path: '',
    component: TaskerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaskerDetailComponent,
    resolve: {
      tasker: TaskerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaskerUpdateComponent,
    resolve: {
      tasker: TaskerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaskerUpdateComponent,
    resolve: {
      tasker: TaskerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(taskerRoute)],
  exports: [RouterModule],
})
export class TaskerRoutingModule {}
