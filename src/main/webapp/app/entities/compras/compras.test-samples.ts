import { ICompras, NewCompras } from './compras.model';

export const sampleWithRequiredData: ICompras = {
  id: 24471,
  detalle: 'privilege nicely barring',
  cantidad: 6107,
  precio: 2343.25,
};

export const sampleWithPartialData: ICompras = {
  id: 19407,
  detalle: 'spectate',
  cantidad: 16404,
  precio: 30585.76,
};

export const sampleWithFullData: ICompras = {
  id: 10664,
  detalle: 'pilot faraway',
  cantidad: 101,
  precio: 14776.95,
};

export const sampleWithNewData: NewCompras = {
  detalle: 'where ew',
  cantidad: 26081,
  precio: 6540.4,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
