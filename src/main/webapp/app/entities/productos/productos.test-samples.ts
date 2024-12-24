import { IProductos, NewProductos } from './productos.model';

export const sampleWithRequiredData: IProductos = {
  id: 626,
  nombre: 'willow',
  cantidad: 16261,
  precio: 1313.67,
};

export const sampleWithPartialData: IProductos = {
  id: 28290,
  nombre: 'improbable diligently feminize',
  cantidad: 23855,
  precio: 9259.26,
};

export const sampleWithFullData: IProductos = {
  id: 25202,
  nombre: 'hypothesise a whereas',
  cantidad: 2019,
  precio: 14476.85,
};

export const sampleWithNewData: NewProductos = {
  nombre: 'anxiously towards besides',
  cantidad: 15085,
  precio: 27007.46,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
