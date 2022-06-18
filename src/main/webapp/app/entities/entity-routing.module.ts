import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'task',
        data: { pageTitle: 'taskateApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'task-category',
        data: { pageTitle: 'taskateApp.taskCategory.home.title' },
        loadChildren: () => import('./task-category/task-category.module').then(m => m.TaskCategoryModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'taskateApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'taskateApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'taskateApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'tasker',
        data: { pageTitle: 'taskateApp.tasker.home.title' },
        loadChildren: () => import('./tasker/tasker.module').then(m => m.TaskerModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'taskateApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
