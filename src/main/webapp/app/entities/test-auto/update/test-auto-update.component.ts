import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRapport } from 'app/entities/rapport/rapport.model';
import { RapportService } from 'app/entities/rapport/service/rapport.service';
import { ITestAuto } from '../test-auto.model';
import { TestAutoService } from '../service/test-auto.service';
import { TestAutoFormService, TestAutoFormGroup } from './test-auto-form.service';

@Component({
  standalone: true,
  selector: 'jhi-test-auto-update',
  templateUrl: './test-auto-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestAutoUpdateComponent implements OnInit {
  isSaving = false;
  testAuto: ITestAuto | null = null;

  rapportsSharedCollection: IRapport[] = [];

  editForm: TestAutoFormGroup = this.testAutoFormService.createTestAutoFormGroup();

  constructor(
    protected testAutoService: TestAutoService,
    protected testAutoFormService: TestAutoFormService,
    protected rapportService: RapportService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareRapport = (o1: IRapport | null, o2: IRapport | null): boolean => this.rapportService.compareRapport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testAuto }) => {
      this.testAuto = testAuto;
      if (testAuto) {
        this.updateForm(testAuto);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testAuto = this.testAutoFormService.getTestAuto(this.editForm);
    if (testAuto.id !== null) {
      this.subscribeToSaveResponse(this.testAutoService.update(testAuto));
    } else {
      this.subscribeToSaveResponse(this.testAutoService.create(testAuto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestAuto>>): void {
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

  protected updateForm(testAuto: ITestAuto): void {
    this.testAuto = testAuto;
    this.testAutoFormService.resetForm(this.editForm, testAuto);

    this.rapportsSharedCollection = this.rapportService.addRapportToCollectionIfMissing<IRapport>(
      this.rapportsSharedCollection,
      testAuto.rapport,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.rapportService
      .query()
      .pipe(map((res: HttpResponse<IRapport[]>) => res.body ?? []))
      .pipe(map((rapports: IRapport[]) => this.rapportService.addRapportToCollectionIfMissing<IRapport>(rapports, this.testAuto?.rapport)))
      .subscribe((rapports: IRapport[]) => (this.rapportsSharedCollection = rapports));
  }
}
