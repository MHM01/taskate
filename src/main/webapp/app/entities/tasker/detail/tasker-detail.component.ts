import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITasker } from '../tasker.model';

@Component({
  selector: 'jhi-tasker-detail',
  templateUrl: './tasker-detail.component.html',
})
export class TaskerDetailComponent implements OnInit {
  tasker: ITasker | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tasker }) => {
      this.tasker = tasker;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
