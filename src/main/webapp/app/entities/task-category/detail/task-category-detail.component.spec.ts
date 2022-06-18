import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskCategoryDetailComponent } from './task-category-detail.component';

describe('TaskCategory Management Detail Component', () => {
  let comp: TaskCategoryDetailComponent;
  let fixture: ComponentFixture<TaskCategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaskCategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ taskCategory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TaskCategoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TaskCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load taskCategory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.taskCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
