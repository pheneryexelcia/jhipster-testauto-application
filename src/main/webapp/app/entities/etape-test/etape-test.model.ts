import { ITestAuto } from 'app/entities/test-auto/test-auto.model';

export interface IEtapeTest {
  id: number;
  nom?: string | null;
  status?: string | null;
  infos?: string | null;
  testAuto?: ITestAuto | null;
}

export type NewEtapeTest = Omit<IEtapeTest, 'id'> & { id: null };
