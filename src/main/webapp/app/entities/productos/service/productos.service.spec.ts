import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProductos } from '../productos.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../productos.test-samples';

import { ProductosService } from './productos.service';

const requireRestSample: IProductos = {
  ...sampleWithRequiredData,
};

describe('Productos Service', () => {
  let service: ProductosService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductos | IProductos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductosService);
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

    it('should create a Productos', () => {
      const productos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Productos', () => {
      const productos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Productos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Productos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Productos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductosToCollectionIfMissing', () => {
      it('should add a Productos to an empty array', () => {
        const productos: IProductos = sampleWithRequiredData;
        expectedResult = service.addProductosToCollectionIfMissing([], productos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productos);
      });

      it('should not add a Productos to an array that contains it', () => {
        const productos: IProductos = sampleWithRequiredData;
        const productosCollection: IProductos[] = [
          {
            ...productos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, productos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Productos to an array that doesn't contain it", () => {
        const productos: IProductos = sampleWithRequiredData;
        const productosCollection: IProductos[] = [sampleWithPartialData];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, productos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productos);
      });

      it('should add only unique Productos to an array', () => {
        const productosArray: IProductos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productosCollection: IProductos[] = [sampleWithRequiredData];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, ...productosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productos: IProductos = sampleWithRequiredData;
        const productos2: IProductos = sampleWithPartialData;
        expectedResult = service.addProductosToCollectionIfMissing([], productos, productos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productos);
        expect(expectedResult).toContain(productos2);
      });

      it('should accept null and undefined values', () => {
        const productos: IProductos = sampleWithRequiredData;
        expectedResult = service.addProductosToCollectionIfMissing([], null, productos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productos);
      });

      it('should return initial array if no Productos is added', () => {
        const productosCollection: IProductos[] = [sampleWithRequiredData];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, undefined, null);
        expect(expectedResult).toEqual(productosCollection);
      });
    });

    describe('compareProductos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductos(entity1, entity2);
        const compareResult2 = service.compareProductos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductos(entity1, entity2);
        const compareResult2 = service.compareProductos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductos(entity1, entity2);
        const compareResult2 = service.compareProductos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
