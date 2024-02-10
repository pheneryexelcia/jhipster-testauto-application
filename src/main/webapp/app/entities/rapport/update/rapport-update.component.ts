import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRapport } from '../rapport.model';
import { RapportService } from '../service/rapport.service';
import { RapportFormService, RapportFormGroup } from './rapport-form.service';

@Component({
  standalone: true,
  selector: 'jhi-rapport-update',
  templateUrl: './rapport-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RapportUpdateComponent implements OnInit {
  isSaving = false;
  rapport: IRapport | null = null;

  editForm: RapportFormGroup = this.rapportFormService.createRapportFormGroup();

  constructor(
    protected rapportService: RapportService,
    protected rapportFormService: RapportFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapport }) => {
      this.rapport = rapport;
      if (rapport) {
        this.updateForm(rapport);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rapport = this.rapportFormService.getRapport(this.editForm);
    if (rapport.id !== null) {
      this.subscribeToSaveResponse(this.rapportService.update(rapport));
    } else {
      this.subscribeToSaveResponse(this.rapportService.create(rapport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRapport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(rapport: IRapport): void {
    this.rapport = rapport;
    this.rapportFormService.resetForm(this.editForm, rapport);
  }
}
