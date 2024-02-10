import { IEtapeTest } from 'app/entities/etape-test/etape-test.model';
import { IRapport } from 'app/entities/rapport/rapport.model';

export interface ITestAuto {
  id: number;
  nom?: string | null;
  status?: string | null;
  categorie?: string | null;
  infos?: string | null;
  etapeTests?: IEtapeTest[] | null;
  rapport?: IRapport | null;
}

export type NewTestAuto = Omit<ITestAuto, 'id'> & { id: null };
