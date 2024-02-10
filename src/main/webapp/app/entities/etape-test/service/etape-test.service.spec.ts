import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEtapeTest } from '../etape-test.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../etape-test.test-samples';

import { EtapeTestService } from './etape-test.service';

const requireRestSample: IEtapeTest = {
  ...sampleWithRequiredData,
};

describe('EtapeTest Service', () => {
  let service: EtapeTestService;
  let httpMock: HttpTestingController;
  let expectedResult: IEtapeTest | IEtapeTest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EtapeTestService);
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

    it('should create a EtapeTest', () => {
      const etapeTest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(etapeTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EtapeTest', () => {
      const etapeTest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(etapeTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EtapeTest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EtapeTest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EtapeTest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEtapeTestToCollectionIfMissing', () => {
      it('should add a EtapeTest to an empty array', () => {
        const etapeTest: IEtapeTest = sampleWithRequiredData;
        expectedResult = service.addEtapeTestToCollectionIfMissing([], etapeTest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etapeTest);
      });

      it('should not add a EtapeTest to an array that contains it', () => {
        const etapeTest: IEtapeTest = sampleWithRequiredData;
        const etapeTestCollection: IEtapeTest[] = [
          {
            ...etapeTest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEtapeTestToCollectionIfMissing(etapeTestCollection, etapeTest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EtapeTest to an array that doesn't contain it", () => {
        const etapeTest: IEtapeTest = sampleWithRequiredData;
        const etapeTestCollection: IEtapeTest[] = [sampleWithPartialData];
        expectedResult = service.addEtapeTestToCollectionIfMissing(etapeTestCollection, etapeTest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etapeTest);
      });

      it('should add only unique EtapeTest to an array', () => {
        const etapeTestArray: IEtapeTest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const etapeTestCollection: IEtapeTest[] = [sampleWithRequiredData];
        expectedResult = service.addEtapeTestToCollectionIfMissing(etapeTestCollection, ...etapeTestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const etapeTest: IEtapeTest = sampleWithRequiredData;
        const etapeTest2: IEtapeTest = sampleWithPartialData;
        expectedResult = service.addEtapeTestToCollectionIfMissing([], etapeTest, etapeTest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etapeTest);
        expect(expectedResult).toContain(etapeTest2);
      });

      it('should accept null and undefined values', () => {
        const etapeTest: IEtapeTest = sampleWithRequiredData;
        expectedResult = service.addEtapeTestToCollectionIfMissing([], null, etapeTest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etapeTest);
      });

      it('should return initial array if no EtapeTest is added', () => {
        const etapeTestCollection: IEtapeTest[] = [sampleWithRequiredData];
        expectedResult = service.addEtapeTestToCollectionIfMissing(etapeTestCollection, undefined, null);
        expect(expectedResult).toEqual(etapeTestCollection);
      });
    });

    describe('compareEtapeTest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEtapeTest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEtapeTest(entity1, entity2);
        const compareResult2 = service.compareEtapeTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEtapeTest(entity1, entity2);
        const compareResult2 = service.compareEtapeTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEtapeTest(entity1, entity2);
        const compareResult2 = service.compareEtapeTest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
