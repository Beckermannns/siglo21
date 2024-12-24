import { IVentas, NewVentas } from './ventas.model';

export const sampleWithRequiredData: IVentas = {
  id: 15981,
  descripcion: 'before boom',
  cantidad: 8537,
  total: 11820.33,
};

export const sampleWithPartialData: IVentas = {
  id: 1651,
  descripcion: 'upside-down knit good-natured',
  cantidad: 13360,
  total: 31962.82,
};

export const sampleWithFullData: IVentas = {
  id: 16847,
  descripcion: 'sweetly',
  cantidad: 17760,
  total: 9792.34,
};

export const sampleWithNewData: NewVentas = {
  descripcion: 'taro unlike attribute',
  cantidad: 12134,
  total: 6409.91,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
