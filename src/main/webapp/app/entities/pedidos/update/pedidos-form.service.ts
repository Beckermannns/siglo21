import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPedidos, NewPedidos } from '../pedidos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPedidos for edit and NewPedidosFormGroupInput for create.
 */
type PedidosFormGroupInput = IPedidos | PartialWithRequiredKeyOf<NewPedidos>;

type PedidosFormDefaults = Pick<NewPedidos, 'id' | 'estado'>;

type PedidosFormGroupContent = {
  id: FormControl<IPedidos['id'] | NewPedidos['id']>;
  descripcion: FormControl<IPedidos['descripcion']>;
  estado: FormControl<IPedidos['estado']>;
};

export type PedidosFormGroup = FormGroup<PedidosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PedidosFormService {
  createPedidosFormGroup(pedidos: PedidosFormGroupInput = { id: null }): PedidosFormGroup {
    const pedidosRawValue = {
      ...this.getFormDefaults(),
      ...pedidos,
    };
    return new FormGroup<PedidosFormGroupContent>({
      id: new FormControl(
        { value: pedidosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descripcion: new FormControl(pedidosRawValue.descripcion, {
        validators: [Validators.required],
      }),
      estado: new FormControl(pedidosRawValue.estado),
    });
  }

  getPedidos(form: PedidosFormGroup): IPedidos | NewPedidos {
    return form.getRawValue() as IPedidos | NewPedidos;
  }

  resetForm(form: PedidosFormGroup, pedidos: PedidosFormGroupInput): void {
    const pedidosRawValue = { ...this.getFormDefaults(), ...pedidos };
    form.reset(
      {
        ...pedidosRawValue,
        id: { value: pedidosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PedidosFormDefaults {
    return {
      id: null,
      estado: false,
    };
  }
}
