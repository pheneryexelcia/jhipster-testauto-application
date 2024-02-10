import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRapport, NewRapport } from '../rapport.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRapport for edit and NewRapportFormGroupInput for create.
 */
type RapportFormGroupInput = IRapport | PartialWithRequiredKeyOf<NewRapport>;

type RapportFormDefaults = Pick<NewRapport, 'id'>;

type RapportFormGroupContent = {
  id: FormControl<IRapport['id'] | NewRapport['id']>;
  nom: FormControl<IRapport['nom']>;
  dateStr: FormControl<IRapport['dateStr']>;
  environnement: FormControl<IRapport['environnement']>;
  nbtests: FormControl<IRapport['nbtests']>;
  nbtestsOk: FormControl<IRapport['nbtestsOk']>;
  nbtestsKo: FormControl<IRapport['nbtestsKo']>;
  logo: FormControl<IRapport['logo']>;
};

export type RapportFormGroup = FormGroup<RapportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RapportFormService {
  createRapportFormGroup(rapport: RapportFormGroupInput = { id: null }): RapportFormGroup {
    const rapportRawValue = {
      ...this.getFormDefaults(),
      ...rapport,
    };
    return new FormGroup<RapportFormGroupContent>({
      id: new FormControl(
        { value: rapportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(rapportRawValue.nom, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      dateStr: new FormControl(rapportRawValue.dateStr, {
        validators: [Validators.required],
      }),
      environnement: new FormControl(rapportRawValue.environnement, {
        validators: [Validators.required],
      }),
      nbtests: new FormControl(rapportRawValue.nbtests),
      nbtestsOk: new FormControl(rapportRawValue.nbtestsOk),
      nbtestsKo: new FormControl(rapportRawValue.nbtestsKo),
      logo: new FormControl(rapportRawValue.logo),
    });
  }

  getRapport(form: RapportFormGroup): IRapport | NewRapport {
    return form.getRawValue() as IRapport | NewRapport;
  }

  resetForm(form: RapportFormGroup, rapport: RapportFormGroupInput): void {
    const rapportRawValue = { ...this.getFormDefaults(), ...rapport };
    form.reset(
      {
        ...rapportRawValue,
        id: { value: rapportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RapportFormDefaults {
    return {
      id: null,
    };
  }
}
