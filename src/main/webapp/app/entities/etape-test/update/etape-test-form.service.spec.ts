import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../etape-test.test-samples';

import { EtapeTestFormService } from './etape-test-form.service';

describe('EtapeTest Form Service', () => {
  let service: EtapeTestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EtapeTestFormService);
  });

  describe('Service methods', () => {
    describe('createEtapeTestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEtapeTestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            status: expect.any(Object),
            infos: expect.any(Object),
            testAuto: expect.any(Object),
          }),
        );
      });

      it('passing IEtapeTest should create a new form with FormGroup', () => {
        const formGroup = service.createEtapeTestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            status: expect.any(Object),
            infos: expect.any(Object),
            testAuto: expect.any(Object),
          }),
        );
      });
    });

    describe('getEtapeTest', () => {
      it('should return NewEtapeTest for default EtapeTest initial value', () => {
        const formGroup = service.createEtapeTestFormGroup(sampleWithNewData);

        const etapeTest = service.getEtapeTest(formGroup) as any;

        expect(etapeTest).toMatchObject(sampleWithNewData);
      });

      it('should return NewEtapeTest for empty EtapeTest initial value', () => {
        const formGroup = service.createEtapeTestFormGroup();

        const etapeTest = service.getEtapeTest(formGroup) as any;

        expect(etapeTest).toMatchObject({});
      });

      it('should return IEtapeTest', () => {
        const formGroup = service.createEtapeTestFormGroup(sampleWithRequiredData);

        const etapeTest = service.getEtapeTest(formGroup) as any;

        expect(etapeTest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEtapeTest should not enable id FormControl', () => {
        const formGroup = service.createEtapeTestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEtapeTest should disable id FormControl', () => {
        const formGroup = service.createEtapeTestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
