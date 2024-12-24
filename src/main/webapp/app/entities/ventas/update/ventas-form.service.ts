import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVentas, NewVentas } from '../ventas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVentas for edit and NewVentasFormGroupInput for create.
 */
type VentasFormGroupInput = IVentas | PartialWithRequiredKeyOf<NewVentas>;

type VentasFormDefaults = Pick<NewVentas, 'id'>;

type VentasFormGroupContent = {
  id: FormControl<IVentas['id'] | NewVentas['id']>;
  descripcion: FormControl<IVentas['descripcion']>;
  cantidad: FormControl<IVentas['cantidad']>;
  total: FormControl<IVentas['total']>;
};

export type VentasFormGroup = FormGroup<VentasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VentasFormService {
  createVentasFormGroup(ventas: VentasFormGroupInput = { id: null }): VentasFormGroup {
    const ventasRawValue = {
      ...this.getFormDefaults(),
      ...ventas,
    };
    return new FormGroup<VentasFormGroupContent>({
      id: new FormControl(
        { value: ventasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descripcion: new FormControl(ventasRawValue.descripcion, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      cantidad: new FormControl(ventasRawValue.cantidad, {
        validators: [Validators.required],
      }),
      total: new FormControl(ventasRawValue.total, {
        validators: [Validators.required],
      }),
    });
  }

  getVentas(form: VentasFormGroup): IVentas | NewVentas {
    return form.getRawValue() as IVentas | NewVentas;
  }

  resetForm(form: VentasFormGroup, ventas: VentasFormGroupInput): void {
    const ventasRawValue = { ...this.getFormDefaults(), ...ventas };
    form.reset(
      {
        ...ventasRawValue,
        id: { value: ventasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VentasFormDefaults {
    return {
      id: null,
    };
  }
}
