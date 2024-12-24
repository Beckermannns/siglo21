import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../compras.test-samples';

import { ComprasFormService } from './compras-form.service';

describe('Compras Form Service', () => {
  let service: ComprasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComprasFormService);
  });

  describe('Service methods', () => {
    describe('createComprasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createComprasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detalle: expect.any(Object),
            cantidad: expect.any(Object),
            precio: expect.any(Object),
          }),
        );
      });

      it('passing ICompras should create a new form with FormGroup', () => {
        const formGroup = service.createComprasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detalle: expect.any(Object),
            cantidad: expect.any(Object),
            precio: expect.any(Object),
          }),
        );
      });
    });

    describe('getCompras', () => {
      it('should return NewCompras for default Compras initial value', () => {
        const formGroup = service.createComprasFormGroup(sampleWithNewData);

        const compras = service.getCompras(formGroup) as any;

        expect(compras).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompras for empty Compras initial value', () => {
        const formGroup = service.createComprasFormGroup();

        const compras = service.getCompras(formGroup) as any;

        expect(compras).toMatchObject({});
      });

      it('should return ICompras', () => {
        const formGroup = service.createComprasFormGroup(sampleWithRequiredData);

        const compras = service.getCompras(formGroup) as any;

        expect(compras).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompras should not enable id FormControl', () => {
        const formGroup = service.createComprasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompras should disable id FormControl', () => {
        const formGroup = service.createComprasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
