import { ITestAuto } from 'app/entities/test-auto/test-auto.model';

export interface IRapport {
  id: number;
  nom?: string | null;
  dateStr?: string | null;
  environnement?: string | null;
  nbtests?: number | null;
  nbtestsOk?: number | null;
  nbtestsKo?: number | null;
  logo?: string | null;
  testAutos?: ITestAuto[] | null;
}

export type NewRapport = Omit<IRapport, 'id'> & { id: null };
