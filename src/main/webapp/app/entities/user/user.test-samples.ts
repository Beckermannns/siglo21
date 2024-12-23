import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 560,
  login: 'md-C@KrY\\W81y6cn\\~Cwlu\\gH\\r03R',
};

export const sampleWithPartialData: IUser = {
  id: 29400,
  login: 'T@Y\\~C33Q\\127PN',
};

export const sampleWithFullData: IUser = {
  id: 30784,
  login: 'Xv9yrh',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
