import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TaskerComponent } from './list/tasker.component';
import { TaskerDetailComponent } from './detail/tasker-detail.component';
import { TaskerUpdateComponent } from './update/tasker-update.component';
import { TaskerDeleteDialogComponent } from './delete/tasker-delete-dialog.component';
import { TaskerRoutingModule } from './route/tasker-routing.module';

@NgModule({
  imports: [SharedModule, TaskerRoutingModule],
  declarations: [TaskerComponent, TaskerDetailComponent, TaskerUpdateComponent, TaskerDeleteDialogComponent],
  entryComponents: [TaskerDeleteDialogComponent],
})
export class TaskerModule {}
