import { IBodega, NewBodega } from './bodega.model';

export const sampleWithRequiredData: IBodega = {
  id: 14350,
  producto: 'nice furthermore',
  cantidad: 26752,
  descripcion: 'ugh famously excepting',
};

export const sampleWithPartialData: IBodega = {
  id: 26650,
  producto: 'acknowledge give midst',
  cantidad: 17042,
  descripcion: 'sushi ill-fated',
};

export const sampleWithFullData: IBodega = {
  id: 5113,
  producto: 'sway zowie toward',
  cantidad: 2680,
  descripcion: 'ultimate',
};

export const sampleWithNewData: NewBodega = {
  producto: 'quarrelsomely phooey dividend',
  cantidad: 32745,
  descripcion: 'truly merrily upset',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
