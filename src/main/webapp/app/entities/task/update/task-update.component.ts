import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITask, Task } from '../task.model';
import { TaskService } from '../service/task.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ITaskCategory } from 'app/entities/task-category/task-category.model';
import { TaskCategoryService } from 'app/entities/task-category/service/task-category.service';
import { ITasker } from 'app/entities/tasker/tasker.model';
import { TaskerService } from 'app/entities/tasker/service/tasker.service';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html',
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;
  taskStatusValues = Object.keys(TaskStatus);

  locationsCollection: ILocation[] = [];
  taskCategoriesCollection: ITaskCategory[] = [];
  taskersSharedCollection: ITasker[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    numberOfTaskerRequired: [],
    budget: [],
    startDate: [],
    status: [],
    location: [],
    taskCategory: [],
    taskers: [],
  });

  constructor(
    protected taskService: TaskService,
    protected locationService: LocationService,
    protected taskCategoryService: TaskCategoryService,
    protected taskerService: TaskerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      if (task.id === undefined) {
        const today = dayjs().startOf('day');
        task.startDate = today;
      }

      this.updateForm(task);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.createFromForm();
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  trackLocationById(_index: number, item: ILocation): number {
    return item.id!;
  }

  trackTaskCategoryById(_index: number, item: ITaskCategory): number {
    return item.id!;
  }

  trackTaskerById(_index: number, item: ITasker): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
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

  protected updateForm(task: ITask): void {
    this.editForm.patchValue({
      id: task.id,
      title: task.title,
      description: task.description,
      numberOfTaskerRequired: task.numberOfTaskerRequired,
      budget: task.budget,
      startDate: task.startDate ? task.startDate.format(DATE_TIME_FORMAT) : null,
      status: task.status,
      location: task.location,
      taskCategory: task.taskCategory,
      taskers: task.taskers,
    });

    this.locationsCollection = this.locationService.addLocationToCollectionIfMissing(this.locationsCollection, task.location);
    this.taskCategoriesCollection = this.taskCategoryService.addTaskCategoryToCollectionIfMissing(
      this.taskCategoriesCollection,
      task.taskCategory
    );
    this.taskersSharedCollection = this.taskerService.addTaskerToCollectionIfMissing(this.taskersSharedCollection, task.taskers);
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query({ filter: 'task-is-null' })
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(locations, this.editForm.get('location')!.value)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsCollection = locations));

    this.taskCategoryService
      .query({ filter: 'task-is-null' })
      .pipe(map((res: HttpResponse<ITaskCategory[]>) => res.body ?? []))
      .pipe(
        map((taskCategories: ITaskCategory[]) =>
          this.taskCategoryService.addTaskCategoryToCollectionIfMissing(taskCategories, this.editForm.get('taskCategory')!.value)
        )
      )
      .subscribe((taskCategories: ITaskCategory[]) => (this.taskCategoriesCollection = taskCategories));

    this.taskerService
      .query()
      .pipe(map((res: HttpResponse<ITasker[]>) => res.body ?? []))
      .pipe(map((taskers: ITasker[]) => this.taskerService.addTaskerToCollectionIfMissing(taskers, this.editForm.get('taskers')!.value)))
      .subscribe((taskers: ITasker[]) => (this.taskersSharedCollection = taskers));
  }

  protected createFromForm(): ITask {
    return {
      ...new Task(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      numberOfTaskerRequired: this.editForm.get(['numberOfTaskerRequired'])!.value,
      budget: this.editForm.get(['budget'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      location: this.editForm.get(['location'])!.value,
      taskCategory: this.editForm.get(['taskCategory'])!.value,
      taskers: this.editForm.get(['taskers'])!.value,
    };
  }
}
