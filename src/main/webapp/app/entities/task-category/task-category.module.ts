import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TaskCategoryComponent } from './list/task-category.component';
import { TaskCategoryDetailComponent } from './detail/task-category-detail.component';
import { TaskCategoryUpdateComponent } from './update/task-category-update.component';
import { TaskCategoryDeleteDialogComponent } from './delete/task-category-delete-dialog.component';
import { TaskCategoryRoutingModule } from './route/task-category-routing.module';

@NgModule({
  imports: [SharedModule, TaskCategoryRoutingModule],
  declarations: [TaskCategoryComponent, TaskCategoryDetailComponent, TaskCategoryUpdateComponent, TaskCategoryDeleteDialogComponent],
  entryComponents: [TaskCategoryDeleteDialogComponent],
})
export class TaskCategoryModule {}
