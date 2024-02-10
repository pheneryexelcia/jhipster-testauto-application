import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../test-auto.test-samples';

import { TestAutoFormService } from './test-auto-form.service';

describe('TestAuto Form Service', () => {
  let service: TestAutoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestAutoFormService);
  });

  describe('Service methods', () => {
    describe('createTestAutoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTestAutoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            status: expect.any(Object),
            categorie: expect.any(Object),
            infos: expect.any(Object),
            rapport: expect.any(Object),
          }),
        );
      });

      it('passing ITestAuto should create a new form with FormGroup', () => {
        const formGroup = service.createTestAutoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            status: expect.any(Object),
            categorie: expect.any(Object),
            infos: expect.any(Object),
            rapport: expect.any(Object),
          }),
        );
      });
    });

    describe('getTestAuto', () => {
      it('should return NewTestAuto for default TestAuto initial value', () => {
        const formGroup = service.createTestAutoFormGroup(sampleWithNewData);

        const testAuto = service.getTestAuto(formGroup) as any;

        expect(testAuto).toMatchObject(sampleWithNewData);
      });

      it('should return NewTestAuto for empty TestAuto initial value', () => {
        const formGroup = service.createTestAutoFormGroup();

        const testAuto = service.getTestAuto(formGroup) as any;

        expect(testAuto).toMatchObject({});
      });

      it('should return ITestAuto', () => {
        const formGroup = service.createTestAutoFormGroup(sampleWithRequiredData);

        const testAuto = service.getTestAuto(formGroup) as any;

        expect(testAuto).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITestAuto should not enable id FormControl', () => {
        const formGroup = service.createTestAutoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTestAuto should disable id FormControl', () => {
        const formGroup = service.createTestAutoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
