import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVentas } from '../ventas.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ventas.test-samples';

import { VentasService } from './ventas.service';

const requireRestSample: IVentas = {
  ...sampleWithRequiredData,
};

describe('Ventas Service', () => {
  let service: VentasService;
  let httpMock: HttpTestingController;
  let expectedResult: IVentas | IVentas[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VentasService);
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

    it('should create a Ventas', () => {
      const ventas = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ventas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ventas', () => {
      const ventas = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ventas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ventas', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ventas', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ventas', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVentasToCollectionIfMissing', () => {
      it('should add a Ventas to an empty array', () => {
        const ventas: IVentas = sampleWithRequiredData;
        expectedResult = service.addVentasToCollectionIfMissing([], ventas);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ventas);
      });

      it('should not add a Ventas to an array that contains it', () => {
        const ventas: IVentas = sampleWithRequiredData;
        const ventasCollection: IVentas[] = [
          {
            ...ventas,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, ventas);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ventas to an array that doesn't contain it", () => {
        const ventas: IVentas = sampleWithRequiredData;
        const ventasCollection: IVentas[] = [sampleWithPartialData];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, ventas);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ventas);
      });

      it('should add only unique Ventas to an array', () => {
        const ventasArray: IVentas[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ventasCollection: IVentas[] = [sampleWithRequiredData];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, ...ventasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ventas: IVentas = sampleWithRequiredData;
        const ventas2: IVentas = sampleWithPartialData;
        expectedResult = service.addVentasToCollectionIfMissing([], ventas, ventas2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ventas);
        expect(expectedResult).toContain(ventas2);
      });

      it('should accept null and undefined values', () => {
        const ventas: IVentas = sampleWithRequiredData;
        expectedResult = service.addVentasToCollectionIfMissing([], null, ventas, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ventas);
      });

      it('should return initial array if no Ventas is added', () => {
        const ventasCollection: IVentas[] = [sampleWithRequiredData];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, undefined, null);
        expect(expectedResult).toEqual(ventasCollection);
      });
    });

    describe('compareVentas', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVentas(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVentas(entity1, entity2);
        const compareResult2 = service.compareVentas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVentas(entity1, entity2);
        const compareResult2 = service.compareVentas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVentas(entity1, entity2);
        const compareResult2 = service.compareVentas(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
