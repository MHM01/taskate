import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TaskerService } from '../service/tasker.service';
import { ITasker, Tasker } from '../tasker.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ITaskCategory } from 'app/entities/task-category/task-category.model';
import { TaskCategoryService } from 'app/entities/task-category/service/task-category.service';

import { TaskerUpdateComponent } from './tasker-update.component';

describe('Tasker Management Update Component', () => {
  let comp: TaskerUpdateComponent;
  let fixture: ComponentFixture<TaskerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taskerService: TaskerService;
  let locationService: LocationService;
  let taskCategoryService: TaskCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TaskerUpdateComponent],
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
      .overrideTemplate(TaskerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaskerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taskerService = TestBed.inject(TaskerService);
    locationService = TestBed.inject(LocationService);
    taskCategoryService = TestBed.inject(TaskCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call address query and add missing value', () => {
      const tasker: ITasker = { id: 456 };
      const address: ILocation = { id: 12191 };
      tasker.address = address;

      const addressCollection: ILocation[] = [{ id: 12262 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: ILocation[] = [address, ...addressCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tasker });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should call TaskCategory query and add missing value', () => {
      const tasker: ITasker = { id: 456 };
      const taskCategories: ITaskCategory = { id: 42370 };
      tasker.taskCategories = taskCategories;

      const taskCategoryCollection: ITaskCategory[] = [{ id: 80257 }];
      jest.spyOn(taskCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: taskCategoryCollection })));
      const additionalTaskCategories = [taskCategories];
      const expectedCollection: ITaskCategory[] = [...additionalTaskCategories, ...taskCategoryCollection];
      jest.spyOn(taskCategoryService, 'addTaskCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tasker });
      comp.ngOnInit();

      expect(taskCategoryService.query).toHaveBeenCalled();
      expect(taskCategoryService.addTaskCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        taskCategoryCollection,
        ...additionalTaskCategories
      );
      expect(comp.taskCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tasker: ITasker = { id: 456 };
      const address: ILocation = { id: 84680 };
      tasker.address = address;
      const taskCategories: ITaskCategory = { id: 1784 };
      tasker.taskCategories = taskCategories;

      activatedRoute.data = of({ tasker });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tasker));
      expect(comp.addressesCollection).toContain(address);
      expect(comp.taskCategoriesSharedCollection).toContain(taskCategories);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tasker>>();
      const tasker = { id: 123 };
      jest.spyOn(taskerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tasker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tasker }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(taskerService.update).toHaveBeenCalledWith(tasker);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tasker>>();
      const tasker = new Tasker();
      jest.spyOn(taskerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tasker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tasker }));
      saveSubject.complete();

      // THEN
      expect(taskerService.create).toHaveBeenCalledWith(tasker);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tasker>>();
      const tasker = { id: 123 };
      jest.spyOn(taskerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tasker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taskerService.update).toHaveBeenCalledWith(tasker);
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
  });
});
