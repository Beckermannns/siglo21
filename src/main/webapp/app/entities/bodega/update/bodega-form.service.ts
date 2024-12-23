import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBodega, NewBodega } from '../bodega.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBodega for edit and NewBodegaFormGroupInput for create.
 */
type BodegaFormGroupInput = IBodega | PartialWithRequiredKeyOf<NewBodega>;

type BodegaFormDefaults = Pick<NewBodega, 'id'>;

type BodegaFormGroupContent = {
  id: FormControl<IBodega['id'] | NewBodega['id']>;
  producto: FormControl<IBodega['producto']>;
  cantidad: FormControl<IBodega['cantidad']>;
  descripcion: FormControl<IBodega['descripcion']>;
};

export type BodegaFormGroup = FormGroup<BodegaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BodegaFormService {
  createBodegaFormGroup(bodega: BodegaFormGroupInput = { id: null }): BodegaFormGroup {
    const bodegaRawValue = {
      ...this.getFormDefaults(),
      ...bodega,
    };
    return new FormGroup<BodegaFormGroupContent>({
      id: new FormControl(
        { value: bodegaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      producto: new FormControl(bodegaRawValue.producto, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(bodegaRawValue.cantidad, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(bodegaRawValue.descripcion, {
        validators: [Validators.required],
      }),
    });
  }

  getBodega(form: BodegaFormGroup): IBodega | NewBodega {
    return form.getRawValue() as IBodega | NewBodega;
  }

  resetForm(form: BodegaFormGroup, bodega: BodegaFormGroupInput): void {
    const bodegaRawValue = { ...this.getFormDefaults(), ...bodega };
    form.reset(
      {
        ...bodegaRawValue,
        id: { value: bodegaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BodegaFormDefaults {
    return {
      id: null,
    };
  }
}
