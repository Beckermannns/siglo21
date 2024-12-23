import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '51fa811a-6497-4672-bcfb-e51a43085fcc',
};

export const sampleWithPartialData: IAuthority = {
  name: 'c2d693b3-0ae5-46d6-a579-507748b9b0de',
};

export const sampleWithFullData: IAuthority = {
  name: 'd7df29e9-2868-4b0a-b966-aa6b31c61f23',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
