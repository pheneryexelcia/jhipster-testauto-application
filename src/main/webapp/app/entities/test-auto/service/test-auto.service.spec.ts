import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITestAuto } from '../test-auto.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../test-auto.test-samples';

import { TestAutoService } from './test-auto.service';

const requireRestSample: ITestAuto = {
  ...sampleWithRequiredData,
};

describe('TestAuto Service', () => {
  let service: TestAutoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITestAuto | ITestAuto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TestAutoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TestAuto', () => {
      const testAuto = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(testAuto).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TestAuto', () => {
      const testAuto = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(testAuto).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TestAuto', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TestAuto', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TestAuto', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTestAutoToCollectionIfMissing', () => {
      it('should add a TestAuto to an empty array', () => {
        const testAuto: ITestAuto = sampleWithRequiredData;
        expectedResult = service.addTestAutoToCollectionIfMissing([], testAuto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testAuto);
      });

      it('should not add a TestAuto to an array that contains it', () => {
        const testAuto: ITestAuto = sampleWithRequiredData;
        const testAutoCollection: ITestAuto[] = [
          {
            ...testAuto,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTestAutoToCollectionIfMissing(testAutoCollection, testAuto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TestAuto to an array that doesn't contain it", () => {
        const testAuto: ITestAuto = sampleWithRequiredData;
        const testAutoCollection: ITestAuto[] = [sampleWithPartialData];
        expectedResult = service.addTestAutoToCollectionIfMissing(testAutoCollection, testAuto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testAuto);
      });

      it('should add only unique TestAuto to an array', () => {
        const testAutoArray: ITestAuto[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const testAutoCollection: ITestAuto[] = [sampleWithRequiredData];
        expectedResult = service.addTestAutoToCollectionIfMissing(testAutoCollection, ...testAutoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testAuto: ITestAuto = sampleWithRequiredData;
        const testAuto2: ITestAuto = sampleWithPartialData;
        expectedResult = service.addTestAutoToCollectionIfMissing([], testAuto, testAuto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testAuto);
        expect(expectedResult).toContain(testAuto2);
      });

      it('should accept null and undefined values', () => {
        const testAuto: ITestAuto = sampleWithRequiredData;
        expectedResult = service.addTestAutoToCollectionIfMissing([], null, testAuto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testAuto);
      });

      it('should return initial array if no TestAuto is added', () => {
        const testAutoCollection: ITestAuto[] = [sampleWithRequiredData];
        expectedResult = service.addTestAutoToCollectionIfMissing(testAutoCollection, undefined, null);
        expect(expectedResult).toEqual(testAutoCollection);
      });
    });

    describe('compareTestAuto', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTestAuto(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTestAuto(entity1, entity2);
        const compareResult2 = service.compareTestAuto(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTestAuto(entity1, entity2);
        const compareResult2 = service.compareTestAuto(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTestAuto(entity1, entity2);
        const compareResult2 = service.compareTestAuto(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
