import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TaskCategoryService } from '../service/task-category.service';
import { ITaskCategory, TaskCategory } from '../task-category.model';

import { TaskCategoryUpdateComponent } from './task-category-update.component';

describe('TaskCategory Management Update Component', () => {
  let comp: TaskCategoryUpdateComponent;
  let fixture: ComponentFixture<TaskCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taskCategoryService: TaskCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TaskCategoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TaskCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaskCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taskCategoryService = TestBed.inject(TaskCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const taskCategory: ITaskCategory = { id: 456 };

      activatedRoute.data = of({ taskCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(taskCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TaskCategory>>();
      const taskCategory = { id: 123 };
      jest.spyOn(taskCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taskCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taskCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(taskCategoryService.update).toHaveBeenCalledWith(taskCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TaskCategory>>();
      const taskCategory = new TaskCategory();
      jest.spyOn(taskCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taskCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taskCategory }));
      saveSubject.complete();

      // THEN
      expect(taskCategoryService.create).toHaveBeenCalledWith(taskCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TaskCategory>>();
      const taskCategory = { id: 123 };
      jest.spyOn(taskCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taskCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taskCategoryService.update).toHaveBeenCalledWith(taskCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
