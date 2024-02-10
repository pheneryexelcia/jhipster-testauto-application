import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITestAuto } from 'app/entities/test-auto/test-auto.model';
import { TestAutoService } from 'app/entities/test-auto/service/test-auto.service';
import { IEtapeTest } from '../etape-test.model';
import { EtapeTestService } from '../service/etape-test.service';
import { EtapeTestFormService, EtapeTestFormGroup } from './etape-test-form.service';

@Component({
  standalone: true,
  selector: 'jhi-etape-test-update',
  templateUrl: './etape-test-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EtapeTestUpdateComponent implements OnInit {
  isSaving = false;
  etapeTest: IEtapeTest | null = null;

  testAutosSharedCollection: ITestAuto[] = [];

  editForm: EtapeTestFormGroup = this.etapeTestFormService.createEtapeTestFormGroup();

  constructor(
    protected etapeTestService: EtapeTestService,
    protected etapeTestFormService: EtapeTestFormService,
    protected testAutoService: TestAutoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTestAuto = (o1: ITestAuto | null, o2: ITestAuto | null): boolean => this.testAutoService.compareTestAuto(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etapeTest }) => {
      this.etapeTest = etapeTest;
      if (etapeTest) {
        this.updateForm(etapeTest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etapeTest = this.etapeTestFormService.getEtapeTest(this.editForm);
    if (etapeTest.id !== null) {
      this.subscribeToSaveResponse(this.etapeTestService.update(etapeTest));
    } else {
      this.subscribeToSaveResponse(this.etapeTestService.create(etapeTest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtapeTest>>): void {
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

  protected updateForm(etapeTest: IEtapeTest): void {
    this.etapeTest = etapeTest;
    this.etapeTestFormService.resetForm(this.editForm, etapeTest);

    this.testAutosSharedCollection = this.testAutoService.addTestAutoToCollectionIfMissing<ITestAuto>(
      this.testAutosSharedCollection,
      etapeTest.testAuto,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.testAutoService
      .query()
      .pipe(map((res: HttpResponse<ITestAuto[]>) => res.body ?? []))
      .pipe(
        map((testAutos: ITestAuto[]) =>
          this.testAutoService.addTestAutoToCollectionIfMissing<ITestAuto>(testAutos, this.etapeTest?.testAuto),
        ),
      )
      .subscribe((testAutos: ITestAuto[]) => (this.testAutosSharedCollection = testAutos));
  }
}
