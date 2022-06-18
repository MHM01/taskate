import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TaskService } from '../service/task.service';
import { ITask, Task } from '../task.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ITaskCategory } from 'app/entities/task-category/task-category.model';
import { TaskCategoryService } from 'app/entities/task-category/service/task-category.service';
import { ITasker } from 'app/entities/tasker/tasker.model';
import { TaskerService } from 'app/entities/tasker/service/tasker.service';

import { TaskUpdateComponent } from './task-update.component';

describe('Task Management Update Component', () => {
  let comp: TaskUpdateComponent;
  let fixture: ComponentFixture<TaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taskService: TaskService;
  let locationService: LocationService;
  let taskCategoryService: TaskCategoryService;
  let taskerService: TaskerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TaskUpdateComponent],
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
      .overrideTemplate(TaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taskService = TestBed.inject(TaskService);
    locationService = TestBed.inject(LocationService);
    taskCategoryService = TestBed.inject(TaskCategoryService);
    taskerService = TestBed.inject(TaskerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call location query and add missing value', () => {
      const task: ITask = { id: 456 };
      const location: ILocation = { id: 50623 };
      task.location = location;

      const locationCollection: ILocation[] = [{ id: 86225 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const expectedCollection: ILocation[] = [location, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, location);
      expect(comp.locationsCollection).toEqual(expectedCollection);
    });

    it('Should call taskCategory query and add missing value', () => {
      const task: ITask = { id: 456 };
      const taskCategory: ITaskCategory = { id: 43598 };
      task.taskCategory = taskCategory;

      const taskCategoryCollection: ITaskCategory[] = [{ id: 34139 }];
      jest.spyOn(taskCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: taskCategoryCollection })));
      const expectedCollection: ITaskCategory[] = [taskCategory, ...taskCategoryCollection];
      jest.spyOn(taskCategoryService, 'addTaskCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(taskCategoryService.query).toHaveBeenCalled();
      expect(taskCategoryService.addTaskCategoryToCollectionIfMissing).toHaveBeenCalledWith(taskCategoryCollection, taskCategory);
      expect(comp.taskCategoriesCollection).toEqual(expectedCollection);
    });

    it('Should call Tasker query and add missing value', () => {
      const task: ITask = { id: 456 };
      const taskers: ITasker = { id: 79061 };
      task.taskers = taskers;

      const taskerCollection: ITasker[] = [{ id: 44708 }];
      jest.spyOn(taskerService, 'query').mockReturnValue(of(new HttpResponse({ body: taskerCollection })));
      const additionalTaskers = [taskers];
      const expectedCollection: ITasker[] = [...additionalTaskers, ...taskerCollection];
      jest.spyOn(taskerService, 'addTaskerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(taskerService.query).toHaveBeenCalled();
      expect(taskerService.addTaskerToCollectionIfMissing).toHaveBeenCalledWith(taskerCollection, ...additionalTaskers);
      expect(comp.taskersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const task: ITask = { id: 456 };
      const location: ILocation = { id: 69694 };
      task.location = location;
      const taskCategory: ITaskCategory = { id: 96522 };
      task.taskCategory = taskCategory;
      const taskers: ITasker = { id: 95699 };
      task.taskers = taskers;

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(task));
      expect(comp.locationsCollection).toContain(location);
      expect(comp.taskCategoriesCollection).toContain(taskCategory);
      expect(comp.taskersSharedCollection).toContain(taskers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Task>>();
      const task = { id: 123 };
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(taskService.update).toHaveBeenCalledWith(task);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Task>>();
      const task = new Task();
      jest.spyOn(taskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(taskService.create).toHaveBeenCalledWith(task);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Task>>();
      const task = { id: 123 };
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taskService.update).toHaveBeenCalledWith(task);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLocationById', () => {
      it('Should return tracked Location primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLocationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTaskCategoryById', () => {
      it('Should return tracked TaskCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTaskCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTaskerById', () => {
      it('Should return tracked Tasker primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTaskerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
