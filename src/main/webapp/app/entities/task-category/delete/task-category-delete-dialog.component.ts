import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskCategory } from '../task-category.model';
import { TaskCategoryService } from '../service/task-category.service';

@Component({
  templateUrl: './task-category-delete-dialog.component.html',
})
export class TaskCategoryDeleteDialogComponent {
  taskCategory?: ITaskCategory;

  constructor(protected taskCategoryService: TaskCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taskCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
