import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TaskCategoryService } from '../service/task-category.service';

import { TaskCategoryComponent } from './task-category.component';

describe('TaskCategory Management Component', () => {
  let comp: TaskCategoryComponent;
  let fixture: ComponentFixture<TaskCategoryComponent>;
  let service: TaskCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TaskCategoryComponent],
    })
      .overrideTemplate(TaskCategoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaskCategoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TaskCategoryService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.taskCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
