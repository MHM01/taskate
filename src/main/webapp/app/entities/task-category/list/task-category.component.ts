import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskCategory } from '../task-category.model';
import { TaskCategoryService } from '../service/task-category.service';
import { TaskCategoryDeleteDialogComponent } from '../delete/task-category-delete-dialog.component';

@Component({
  selector: 'jhi-task-category',
  templateUrl: './task-category.component.html',
})
export class TaskCategoryComponent implements OnInit {
  taskCategories?: ITaskCategory[];
  isLoading = false;

  constructor(protected taskCategoryService: TaskCategoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.taskCategoryService.query().subscribe({
      next: (res: HttpResponse<ITaskCategory[]>) => {
        this.isLoading = false;
        this.taskCategories = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITaskCategory): number {
    return item.id!;
  }

  delete(taskCategory: ITaskCategory): void {
    const modalRef = this.modalService.open(TaskCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.taskCategory = taskCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
