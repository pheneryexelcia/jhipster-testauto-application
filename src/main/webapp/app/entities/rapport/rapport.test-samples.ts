import { IRapport, NewRapport } from './rapport.model';

export const sampleWithRequiredData: IRapport = {
  id: 31041,
  nom: 'doucement combler',
  dateStr: 'euh dring',
  environnement: 'figurer',
};

export const sampleWithPartialData: IRapport = {
  id: 25065,
  nom: 'tant que',
  dateStr: 'ronron antagoniste',
  environnement: 'au-dehors',
  nbtests: 4674,
  nbtestsOk: 18006,
  logo: 'par rapport à adversaire assurer',
};

export const sampleWithFullData: IRapport = {
  id: 4828,
  nom: 'si bien que de peur que innombrable',
  dateStr: 'si',
  environnement: 'drelin pour que',
  nbtests: 16626,
  nbtestsOk: 26703,
  nbtestsKo: 942,
  logo: 'bientôt en guise de euh',
};

export const sampleWithNewData: NewRapport = {
  nom: 'coac coac',
  dateStr: 'raide mairie craindre',
  environnement: 'séparer hé foule',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
