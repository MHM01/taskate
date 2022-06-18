import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITaskCategory, TaskCategory } from '../task-category.model';
import { TaskCategoryService } from '../service/task-category.service';

@Component({
  selector: 'jhi-task-category-update',
  templateUrl: './task-category-update.component.html',
})
export class TaskCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(protected taskCategoryService: TaskCategoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskCategory }) => {
      this.updateForm(taskCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taskCategory = this.createFromForm();
    if (taskCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.taskCategoryService.update(taskCategory));
    } else {
      this.subscribeToSaveResponse(this.taskCategoryService.create(taskCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskCategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(taskCategory: ITaskCategory): void {
    this.editForm.patchValue({
      id: taskCategory.id,
      name: taskCategory.name,
      description: taskCategory.description,
    });
  }

  protected createFromForm(): ITaskCategory {
    return {
      ...new TaskCategory(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
