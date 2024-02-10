import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITestAuto, NewTestAuto } from '../test-auto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestAuto for edit and NewTestAutoFormGroupInput for create.
 */
type TestAutoFormGroupInput = ITestAuto | PartialWithRequiredKeyOf<NewTestAuto>;

type TestAutoFormDefaults = Pick<NewTestAuto, 'id'>;

type TestAutoFormGroupContent = {
  id: FormControl<ITestAuto['id'] | NewTestAuto['id']>;
  nom: FormControl<ITestAuto['nom']>;
  status: FormControl<ITestAuto['status']>;
  categorie: FormControl<ITestAuto['categorie']>;
  infos: FormControl<ITestAuto['infos']>;
  rapport: FormControl<ITestAuto['rapport']>;
};

export type TestAutoFormGroup = FormGroup<TestAutoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestAutoFormService {
  createTestAutoFormGroup(testAuto: TestAutoFormGroupInput = { id: null }): TestAutoFormGroup {
    const testAutoRawValue = {
      ...this.getFormDefaults(),
      ...testAuto,
    };
    return new FormGroup<TestAutoFormGroupContent>({
      id: new FormControl(
        { value: testAutoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(testAutoRawValue.nom, {
        validators: [Validators.required],
      }),
      status: new FormControl(testAutoRawValue.status),
      categorie: new FormControl(testAutoRawValue.categorie),
      infos: new FormControl(testAutoRawValue.infos),
      rapport: new FormControl(testAutoRawValue.rapport),
    });
  }

  getTestAuto(form: TestAutoFormGroup): ITestAuto | NewTestAuto {
    return form.getRawValue() as ITestAuto | NewTestAuto;
  }

  resetForm(form: TestAutoFormGroup, testAuto: TestAutoFormGroupInput): void {
    const testAutoRawValue = { ...this.getFormDefaults(), ...testAuto };
    form.reset(
      {
        ...testAutoRawValue,
        id: { value: testAutoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TestAutoFormDefaults {
    return {
      id: null,
    };
  }
}
