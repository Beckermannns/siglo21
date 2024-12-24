import { IPedidos, NewPedidos } from './pedidos.model';

export const sampleWithRequiredData: IPedidos = {
  id: 30100,
  descripcion: 'even tightly',
};

export const sampleWithPartialData: IPedidos = {
  id: 24450,
  descripcion: 'after markup',
};

export const sampleWithFullData: IPedidos = {
  id: 24961,
  descripcion: 'anti ouch',
  estado: true,
};

export const sampleWithNewData: NewPedidos = {
  descripcion: 'excluding athwart crafty',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
