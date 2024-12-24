import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ventas.test-samples';

import { VentasFormService } from './ventas-form.service';

describe('Ventas Form Service', () => {
  let service: VentasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VentasFormService);
  });

  describe('Service methods', () => {
    describe('createVentasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVentasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
            cantidad: expect.any(Object),
            total: expect.any(Object),
          }),
        );
      });

      it('passing IVentas should create a new form with FormGroup', () => {
        const formGroup = service.createVentasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
            cantidad: expect.any(Object),
            total: expect.any(Object),
          }),
        );
      });
    });

    describe('getVentas', () => {
      it('should return NewVentas for default Ventas initial value', () => {
        const formGroup = service.createVentasFormGroup(sampleWithNewData);

        const ventas = service.getVentas(formGroup) as any;

        expect(ventas).toMatchObject(sampleWithNewData);
      });

      it('should return NewVentas for empty Ventas initial value', () => {
        const formGroup = service.createVentasFormGroup();

        const ventas = service.getVentas(formGroup) as any;

        expect(ventas).toMatchObject({});
      });

      it('should return IVentas', () => {
        const formGroup = service.createVentasFormGroup(sampleWithRequiredData);

        const ventas = service.getVentas(formGroup) as any;

        expect(ventas).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVentas should not enable id FormControl', () => {
        const formGroup = service.createVentasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVentas should disable id FormControl', () => {
        const formGroup = service.createVentasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
