import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICompras, NewCompras } from '../compras.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompras for edit and NewComprasFormGroupInput for create.
 */
type ComprasFormGroupInput = ICompras | PartialWithRequiredKeyOf<NewCompras>;

type ComprasFormDefaults = Pick<NewCompras, 'id'>;

type ComprasFormGroupContent = {
  id: FormControl<ICompras['id'] | NewCompras['id']>;
  detalle: FormControl<ICompras['detalle']>;
  cantidad: FormControl<ICompras['cantidad']>;
  precio: FormControl<ICompras['precio']>;
};

export type ComprasFormGroup = FormGroup<ComprasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ComprasFormService {
  createComprasFormGroup(compras: ComprasFormGroupInput = { id: null }): ComprasFormGroup {
    const comprasRawValue = {
      ...this.getFormDefaults(),
      ...compras,
    };
    return new FormGroup<ComprasFormGroupContent>({
      id: new FormControl(
        { value: comprasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      detalle: new FormControl(comprasRawValue.detalle, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      cantidad: new FormControl(comprasRawValue.cantidad, {
        validators: [Validators.required],
      }),
      precio: new FormControl(comprasRawValue.precio, {
        validators: [Validators.required],
      }),
    });
  }

  getCompras(form: ComprasFormGroup): ICompras | NewCompras {
    return form.getRawValue() as ICompras | NewCompras;
  }

  resetForm(form: ComprasFormGroup, compras: ComprasFormGroupInput): void {
    const comprasRawValue = { ...this.getFormDefaults(), ...compras };
    form.reset(
      {
        ...comprasRawValue,
        id: { value: comprasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ComprasFormDefaults {
    return {
      id: null,
    };
  }
}
