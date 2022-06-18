import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { LicenseStatus } from 'app/entities/enumerations/license-status.model';
import { ITasker, Tasker } from '../tasker.model';

import { TaskerService } from './tasker.service';

describe('Tasker Service', () => {
  let service: TaskerService;
  let httpMock: HttpTestingController;
  let elemDefault: ITasker;
  let expectedResult: ITasker | ITasker[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TaskerService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      email: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
      subscribedAt: currentDate,
      licenseStatus: LicenseStatus.EXPIRED,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          subscribedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Tasker', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          subscribedAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          subscribedAt: currentDate,
        },
        returnedFromService
      );

      service.create(new Tasker()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tasker', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          subscribedAt: currentDate.format(DATE_TIME_FORMAT),
          licenseStatus: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          subscribedAt: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tasker', () => {
      const patchObject = Object.assign(
        {
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          phoneNumber: 'BBBBBB',
        },
        new Tasker()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          subscribedAt: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tasker', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          subscribedAt: currentDate.format(DATE_TIME_FORMAT),
          licenseStatus: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          subscribedAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Tasker', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTaskerToCollectionIfMissing', () => {
      it('should add a Tasker to an empty array', () => {
        const tasker: ITasker = { id: 123 };
        expectedResult = service.addTaskerToCollectionIfMissing([], tasker);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tasker);
      });

      it('should not add a Tasker to an array that contains it', () => {
        const tasker: ITasker = { id: 123 };
        const taskerCollection: ITasker[] = [
          {
            ...tasker,
          },
          { id: 456 },
        ];
        expectedResult = service.addTaskerToCollectionIfMissing(taskerCollection, tasker);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tasker to an array that doesn't contain it", () => {
        const tasker: ITasker = { id: 123 };
        const taskerCollection: ITasker[] = [{ id: 456 }];
        expectedResult = service.addTaskerToCollectionIfMissing(taskerCollection, tasker);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tasker);
      });

      it('should add only unique Tasker to an array', () => {
        const taskerArray: ITasker[] = [{ id: 123 }, { id: 456 }, { id: 12529 }];
        const taskerCollection: ITasker[] = [{ id: 123 }];
        expectedResult = service.addTaskerToCollectionIfMissing(taskerCollection, ...taskerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tasker: ITasker = { id: 123 };
        const tasker2: ITasker = { id: 456 };
        expectedResult = service.addTaskerToCollectionIfMissing([], tasker, tasker2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tasker);
        expect(expectedResult).toContain(tasker2);
      });

      it('should accept null and undefined values', () => {
        const tasker: ITasker = { id: 123 };
        expectedResult = service.addTaskerToCollectionIfMissing([], null, tasker, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tasker);
      });

      it('should return initial array if no Tasker is added', () => {
        const taskerCollection: ITasker[] = [{ id: 123 }];
        expectedResult = service.addTaskerToCollectionIfMissing(taskerCollection, undefined, null);
        expect(expectedResult).toEqual(taskerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
