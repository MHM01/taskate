import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaskCategory } from '../task-category.model';

@Component({
  selector: 'jhi-task-category-detail',
  templateUrl: './task-category-detail.component.html',
})
export class TaskCategoryDetailComponent implements OnInit {
  taskCategory: ITaskCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskCategory }) => {
      this.taskCategory = taskCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
