import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITestAuto } from 'app/entities/test-auto/test-auto.model';
import { TestAutoService } from 'app/entities/test-auto/service/test-auto.service';
import { EtapeTestService } from '../service/etape-test.service';
import { IEtapeTest } from '../etape-test.model';
import { EtapeTestFormService } from './etape-test-form.service';

import { EtapeTestUpdateComponent } from './etape-test-update.component';

describe('EtapeTest Management Update Component', () => {
  let comp: EtapeTestUpdateComponent;
  let fixture: ComponentFixture<EtapeTestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let etapeTestFormService: EtapeTestFormService;
  let etapeTestService: EtapeTestService;
  let testAutoService: TestAutoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EtapeTestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EtapeTestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtapeTestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    etapeTestFormService = TestBed.inject(EtapeTestFormService);
    etapeTestService = TestBed.inject(EtapeTestService);
    testAutoService = TestBed.inject(TestAutoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TestAuto query and add missing value', () => {
      const etapeTest: IEtapeTest = { id: 456 };
      const testAuto: ITestAuto = { id: 8639 };
      etapeTest.testAuto = testAuto;

      const testAutoCollection: ITestAuto[] = [{ id: 12223 }];
      jest.spyOn(testAutoService, 'query').mockReturnValue(of(new HttpResponse({ body: testAutoCollection })));
      const additionalTestAutos = [testAuto];
      const expectedCollection: ITestAuto[] = [...additionalTestAutos, ...testAutoCollection];
      jest.spyOn(testAutoService, 'addTestAutoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ etapeTest });
      comp.ngOnInit();

      expect(testAutoService.query).toHaveBeenCalled();
      expect(testAutoService.addTestAutoToCollectionIfMissing).toHaveBeenCalledWith(
        testAutoCollection,
        ...additionalTestAutos.map(expect.objectContaining),
      );
      expect(comp.testAutosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const etapeTest: IEtapeTest = { id: 456 };
      const testAuto: ITestAuto = { id: 29372 };
      etapeTest.testAuto = testAuto;

      activatedRoute.data = of({ etapeTest });
      comp.ngOnInit();

      expect(comp.testAutosSharedCollection).toContain(testAuto);
      expect(comp.etapeTest).toEqual(etapeTest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtapeTest>>();
      const etapeTest = { id: 123 };
      jest.spyOn(etapeTestFormService, 'getEtapeTest').mockReturnValue(etapeTest);
      jest.spyOn(etapeTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etapeTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etapeTest }));
      saveSubject.complete();

      // THEN
      expect(etapeTestFormService.getEtapeTest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(etapeTestService.update).toHaveBeenCalledWith(expect.objectContaining(etapeTest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtapeTest>>();
      const etapeTest = { id: 123 };
      jest.spyOn(etapeTestFormService, 'getEtapeTest').mockReturnValue({ id: null });
      jest.spyOn(etapeTestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etapeTest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etapeTest }));
      saveSubject.complete();

      // THEN
      expect(etapeTestFormService.getEtapeTest).toHaveBeenCalled();
      expect(etapeTestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtapeTest>>();
      const etapeTest = { id: 123 };
      jest.spyOn(etapeTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etapeTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(etapeTestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTestAuto', () => {
      it('Should forward to testAutoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(testAutoService, 'compareTestAuto');
        comp.compareTestAuto(entity, entity2);
        expect(testAutoService.compareTestAuto).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
