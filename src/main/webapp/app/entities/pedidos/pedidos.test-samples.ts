import { IPedidos, NewPedidos } from './pedidos.model';

export const sampleWithRequiredData: IPedidos = {
  id: 11494,
  descripcion: 'king',
};

export const sampleWithPartialData: IPedidos = {
  id: 24053,
  descripcion: 'wombat meaningfully versus',
};

export const sampleWithFullData: IPedidos = {
  id: 1323,
  descripcion: 'blah',
  estado: true,
};

export const sampleWithNewData: NewPedidos = {
  descripcion: 'likewise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
