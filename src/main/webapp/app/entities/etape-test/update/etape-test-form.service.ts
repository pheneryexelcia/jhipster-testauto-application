import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEtapeTest, NewEtapeTest } from '../etape-test.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEtapeTest for edit and NewEtapeTestFormGroupInput for create.
 */
type EtapeTestFormGroupInput = IEtapeTest | PartialWithRequiredKeyOf<NewEtapeTest>;

type EtapeTestFormDefaults = Pick<NewEtapeTest, 'id'>;

type EtapeTestFormGroupContent = {
  id: FormControl<IEtapeTest['id'] | NewEtapeTest['id']>;
  nom: FormControl<IEtapeTest['nom']>;
  status: FormControl<IEtapeTest['status']>;
  infos: FormControl<IEtapeTest['infos']>;
  testAuto: FormControl<IEtapeTest['testAuto']>;
};

export type EtapeTestFormGroup = FormGroup<EtapeTestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EtapeTestFormService {
  createEtapeTestFormGroup(etapeTest: EtapeTestFormGroupInput = { id: null }): EtapeTestFormGroup {
    const etapeTestRawValue = {
      ...this.getFormDefaults(),
      ...etapeTest,
    };
    return new FormGroup<EtapeTestFormGroupContent>({
      id: new FormControl(
        { value: etapeTestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(etapeTestRawValue.nom, {
        validators: [Validators.required],
      }),
      status: new FormControl(etapeTestRawValue.status),
      infos: new FormControl(etapeTestRawValue.infos),
      testAuto: new FormControl(etapeTestRawValue.testAuto),
    });
  }

  getEtapeTest(form: EtapeTestFormGroup): IEtapeTest | NewEtapeTest {
    return form.getRawValue() as IEtapeTest | NewEtapeTest;
  }

  resetForm(form: EtapeTestFormGroup, etapeTest: EtapeTestFormGroupInput): void {
    const etapeTestRawValue = { ...this.getFormDefaults(), ...etapeTest };
    form.reset(
      {
        ...etapeTestRawValue,
        id: { value: etapeTestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EtapeTestFormDefaults {
    return {
      id: null,
    };
  }
}
