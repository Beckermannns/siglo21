import { IBodega, NewBodega } from './bodega.model';

export const sampleWithRequiredData: IBodega = {
  id: 25474,
  producto: 'minty wildly minor',
  cantidad: 2533,
  descripcion: 'oh pillbox fond',
};

export const sampleWithPartialData: IBodega = {
  id: 31553,
  producto: 'beside broadly',
  cantidad: 18741,
  descripcion: 'equatorial where mmm',
};

export const sampleWithFullData: IBodega = {
  id: 30509,
  producto: 'mmm yowza',
  cantidad: 4918,
  descripcion: 'wallaby incidentally',
};

export const sampleWithNewData: NewBodega = {
  producto: 'meanwhile so yippee',
  cantidad: 6628,
  descripcion: 'last versus quickly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
