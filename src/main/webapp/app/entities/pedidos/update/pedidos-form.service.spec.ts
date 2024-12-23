import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pedidos.test-samples';

import { PedidosFormService } from './pedidos-form.service';

describe('Pedidos Form Service', () => {
  let service: PedidosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PedidosFormService);
  });

  describe('Service methods', () => {
    describe('createPedidosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPedidosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
            estado: expect.any(Object),
          }),
        );
      });

      it('passing IPedidos should create a new form with FormGroup', () => {
        const formGroup = service.createPedidosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
            estado: expect.any(Object),
          }),
        );
      });
    });

    describe('getPedidos', () => {
      it('should return NewPedidos for default Pedidos initial value', () => {
        const formGroup = service.createPedidosFormGroup(sampleWithNewData);

        const pedidos = service.getPedidos(formGroup) as any;

        expect(pedidos).toMatchObject(sampleWithNewData);
      });

      it('should return NewPedidos for empty Pedidos initial value', () => {
        const formGroup = service.createPedidosFormGroup();

        const pedidos = service.getPedidos(formGroup) as any;

        expect(pedidos).toMatchObject({});
      });

      it('should return IPedidos', () => {
        const formGroup = service.createPedidosFormGroup(sampleWithRequiredData);

        const pedidos = service.getPedidos(formGroup) as any;

        expect(pedidos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPedidos should not enable id FormControl', () => {
        const formGroup = service.createPedidosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPedidos should disable id FormControl', () => {
        const formGroup = service.createPedidosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
