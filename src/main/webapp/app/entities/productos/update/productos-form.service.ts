import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductos, NewProductos } from '../productos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductos for edit and NewProductosFormGroupInput for create.
 */
type ProductosFormGroupInput = IProductos | PartialWithRequiredKeyOf<NewProductos>;

type ProductosFormDefaults = Pick<NewProductos, 'id'>;

type ProductosFormGroupContent = {
  id: FormControl<IProductos['id'] | NewProductos['id']>;
  nombre: FormControl<IProductos['nombre']>;
  cantidad: FormControl<IProductos['cantidad']>;
  precio: FormControl<IProductos['precio']>;
};

export type ProductosFormGroup = FormGroup<ProductosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductosFormService {
  createProductosFormGroup(productos: ProductosFormGroupInput = { id: null }): ProductosFormGroup {
    const productosRawValue = {
      ...this.getFormDefaults(),
      ...productos,
    };
    return new FormGroup<ProductosFormGroupContent>({
      id: new FormControl(
        { value: productosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(productosRawValue.nombre, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(productosRawValue.cantidad, {
        validators: [Validators.required],
      }),
      precio: new FormControl(productosRawValue.precio, {
        validators: [Validators.required],
      }),
    });
  }

  getProductos(form: ProductosFormGroup): IProductos | NewProductos {
    return form.getRawValue() as IProductos | NewProductos;
  }

  resetForm(form: ProductosFormGroup, productos: ProductosFormGroupInput): void {
    const productosRawValue = { ...this.getFormDefaults(), ...productos };
    form.reset(
      {
        ...productosRawValue,
        id: { value: productosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductosFormDefaults {
    return {
      id: null,
    };
  }
}
