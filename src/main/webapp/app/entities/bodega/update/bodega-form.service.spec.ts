import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bodega.test-samples';

import { BodegaFormService } from './bodega-form.service';

describe('Bodega Form Service', () => {
  let service: BodegaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BodegaFormService);
  });

  describe('Service methods', () => {
    describe('createBodegaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBodegaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            producto: expect.any(Object),
            cantidad: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });

      it('passing IBodega should create a new form with FormGroup', () => {
        const formGroup = service.createBodegaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            producto: expect.any(Object),
            cantidad: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });
    });

    describe('getBodega', () => {
      it('should return NewBodega for default Bodega initial value', () => {
        const formGroup = service.createBodegaFormGroup(sampleWithNewData);

        const bodega = service.getBodega(formGroup) as any;

        expect(bodega).toMatchObject(sampleWithNewData);
      });

      it('should return NewBodega for empty Bodega initial value', () => {
        const formGroup = service.createBodegaFormGroup();

        const bodega = service.getBodega(formGroup) as any;

        expect(bodega).toMatchObject({});
      });

      it('should return IBodega', () => {
        const formGroup = service.createBodegaFormGroup(sampleWithRequiredData);

        const bodega = service.getBodega(formGroup) as any;

        expect(bodega).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBodega should not enable id FormControl', () => {
        const formGroup = service.createBodegaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBodega should disable id FormControl', () => {
        const formGroup = service.createBodegaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
