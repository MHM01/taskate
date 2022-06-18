import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITasker } from '../tasker.model';
import { TaskerService } from '../service/tasker.service';

@Component({
  templateUrl: './tasker-delete-dialog.component.html',
})
export class TaskerDeleteDialogComponent {
  tasker?: ITasker;

  constructor(protected taskerService: TaskerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taskerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
