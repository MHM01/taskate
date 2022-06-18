import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITasker, Tasker } from '../tasker.model';
import { TaskerService } from '../service/tasker.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ITaskCategory } from 'app/entities/task-category/task-category.model';
import { TaskCategoryService } from 'app/entities/task-category/service/task-category.service';
import { LicenseStatus } from 'app/entities/enumerations/license-status.model';

@Component({
  selector: 'jhi-tasker-update',
  templateUrl: './tasker-update.component.html',
})
export class TaskerUpdateComponent implements OnInit {
  isSaving = false;
  licenseStatusValues = Object.keys(LicenseStatus);

  addressesCollection: ILocation[] = [];
  taskCategoriesSharedCollection: ITaskCategory[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    phoneNumber: [],
    subscribedAt: [],
    licenseStatus: [],
    address: [],
    taskCategories: [],
  });

  constructor(
    protected taskerService: TaskerService,
    protected locationService: LocationService,
    protected taskCategoryService: TaskCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tasker }) => {
      if (tasker.id === undefined) {
        const today = dayjs().startOf('day');
        tasker.subscribedAt = today;
      }

      this.updateForm(tasker);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tasker = this.createFromForm();
    if (tasker.id !== undefined) {
      this.subscribeToSaveResponse(this.taskerService.update(tasker));
    } else {
      this.subscribeToSaveResponse(this.taskerService.create(tasker));
    }
  }

  trackLocationById(_index: number, item: ILocation): number {
    return item.id!;
  }

  trackTaskCategoryById(_index: number, item: ITaskCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITasker>>): void {
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

  protected updateForm(tasker: ITasker): void {
    this.editForm.patchValue({
      id: tasker.id,
      firstName: tasker.firstName,
      lastName: tasker.lastName,
      email: tasker.email,
      phoneNumber: tasker.phoneNumber,
      subscribedAt: tasker.subscribedAt ? tasker.subscribedAt.format(DATE_TIME_FORMAT) : null,
      licenseStatus: tasker.licenseStatus,
      address: tasker.address,
      taskCategories: tasker.taskCategories,
    });

    this.addressesCollection = this.locationService.addLocationToCollectionIfMissing(this.addressesCollection, tasker.address);
    this.taskCategoriesSharedCollection = this.taskCategoryService.addTaskCategoryToCollectionIfMissing(
      this.taskCategoriesSharedCollection,
      tasker.taskCategories
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query({ filter: 'tasker-is-null' })
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(locations, this.editForm.get('address')!.value)
        )
      )
      .subscribe((locations: ILocation[]) => (this.addressesCollection = locations));

    this.taskCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITaskCategory[]>) => res.body ?? []))
      .pipe(
        map((taskCategories: ITaskCategory[]) =>
          this.taskCategoryService.addTaskCategoryToCollectionIfMissing(taskCategories, this.editForm.get('taskCategories')!.value)
        )
      )
      .subscribe((taskCategories: ITaskCategory[]) => (this.taskCategoriesSharedCollection = taskCategories));
  }

  protected createFromForm(): ITasker {
    return {
      ...new Tasker(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      subscribedAt: this.editForm.get(['subscribedAt'])!.value
        ? dayjs(this.editForm.get(['subscribedAt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      licenseStatus: this.editForm.get(['licenseStatus'])!.value,
      address: this.editForm.get(['address'])!.value,
      taskCategories: this.editForm.get(['taskCategories'])!.value,
    };
  }
}
