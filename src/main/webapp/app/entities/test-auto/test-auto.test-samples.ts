import { ITestAuto, NewTestAuto } from './test-auto.model';

export const sampleWithRequiredData: ITestAuto = {
  id: 2538,
  nom: 'contre équipe de recherche guère',
};

export const sampleWithPartialData: ITestAuto = {
  id: 31895,
  nom: 'hormis',
  status: 'de crainte que près de membre titulaire',
  categorie: 'repentir placide',
  infos: 'au-dedans de quasiment',
};

export const sampleWithFullData: ITestAuto = {
  id: 6868,
  nom: 'de crainte que',
  status: 'rudement environ',
  categorie: 'baiser',
  infos: 'porte-parole parlementaire comme',
};

export const sampleWithNewData: NewTestAuto = {
  nom: 'au prix de tandis que parlementaire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
