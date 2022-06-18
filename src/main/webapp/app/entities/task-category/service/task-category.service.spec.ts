import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITaskCategory, TaskCategory } from '../task-category.model';

import { TaskCategoryService } from './task-category.service';

describe('TaskCategory Service', () => {
  let service: TaskCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: ITaskCategory;
  let expectedResult: ITaskCategory | ITaskCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TaskCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TaskCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TaskCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TaskCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TaskCategory', () => {
      const patchObject = Object.assign({}, new TaskCategory());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TaskCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TaskCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTaskCategoryToCollectionIfMissing', () => {
      it('should add a TaskCategory to an empty array', () => {
        const taskCategory: ITaskCategory = { id: 123 };
        expectedResult = service.addTaskCategoryToCollectionIfMissing([], taskCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taskCategory);
      });

      it('should not add a TaskCategory to an array that contains it', () => {
        const taskCategory: ITaskCategory = { id: 123 };
        const taskCategoryCollection: ITaskCategory[] = [
          {
            ...taskCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addTaskCategoryToCollectionIfMissing(taskCategoryCollection, taskCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TaskCategory to an array that doesn't contain it", () => {
        const taskCategory: ITaskCategory = { id: 123 };
        const taskCategoryCollection: ITaskCategory[] = [{ id: 456 }];
        expectedResult = service.addTaskCategoryToCollectionIfMissing(taskCategoryCollection, taskCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taskCategory);
      });

      it('should add only unique TaskCategory to an array', () => {
        const taskCategoryArray: ITaskCategory[] = [{ id: 123 }, { id: 456 }, { id: 62009 }];
        const taskCategoryCollection: ITaskCategory[] = [{ id: 123 }];
        expectedResult = service.addTaskCategoryToCollectionIfMissing(taskCategoryCollection, ...taskCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taskCategory: ITaskCategory = { id: 123 };
        const taskCategory2: ITaskCategory = { id: 456 };
        expectedResult = service.addTaskCategoryToCollectionIfMissing([], taskCategory, taskCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taskCategory);
        expect(expectedResult).toContain(taskCategory2);
      });

      it('should accept null and undefined values', () => {
        const taskCategory: ITaskCategory = { id: 123 };
        expectedResult = service.addTaskCategoryToCollectionIfMissing([], null, taskCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taskCategory);
      });

      it('should return initial array if no TaskCategory is added', () => {
        const taskCategoryCollection: ITaskCategory[] = [{ id: 123 }];
        expectedResult = service.addTaskCategoryToCollectionIfMissing(taskCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(taskCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
