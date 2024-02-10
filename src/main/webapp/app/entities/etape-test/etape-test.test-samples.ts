import { IEtapeTest, NewEtapeTest } from './etape-test.model';

export const sampleWithRequiredData: IEtapeTest = {
  id: 12633,
  nom: 'assez athlète',
};

export const sampleWithPartialData: IEtapeTest = {
  id: 18440,
  nom: 'clientèle',
  infos: 'toc-toc atteindre',
};

export const sampleWithFullData: IEtapeTest = {
  id: 5036,
  nom: 'dense',
  status: 'bè de manière à ce que',
  infos: 'fidèle crac hôte',
};

export const sampleWithNewData: NewEtapeTest = {
  nom: 'plus disposer spécialiste',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
