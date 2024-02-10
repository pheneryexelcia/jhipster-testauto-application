import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRapport } from 'app/entities/rapport/rapport.model';
import { RapportService } from 'app/entities/rapport/service/rapport.service';
import { TestAutoService } from '../service/test-auto.service';
import { ITestAuto } from '../test-auto.model';
import { TestAutoFormService } from './test-auto-form.service';

import { TestAutoUpdateComponent } from './test-auto-update.component';

describe('TestAuto Management Update Component', () => {
  let comp: TestAutoUpdateComponent;
  let fixture: ComponentFixture<TestAutoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testAutoFormService: TestAutoFormService;
  let testAutoService: TestAutoService;
  let rapportService: RapportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TestAutoUpdateComponent],
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
      .overrideTemplate(TestAutoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestAutoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testAutoFormService = TestBed.inject(TestAutoFormService);
    testAutoService = TestBed.inject(TestAutoService);
    rapportService = TestBed.inject(RapportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Rapport query and add missing value', () => {
      const testAuto: ITestAuto = { id: 456 };
      const rapport: IRapport = { id: 27095 };
      testAuto.rapport = rapport;

      const rapportCollection: IRapport[] = [{ id: 14255 }];
      jest.spyOn(rapportService, 'query').mockReturnValue(of(new HttpResponse({ body: rapportCollection })));
      const additionalRapports = [rapport];
      const expectedCollection: IRapport[] = [...additionalRapports, ...rapportCollection];
      jest.spyOn(rapportService, 'addRapportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testAuto });
      comp.ngOnInit();

      expect(rapportService.query).toHaveBeenCalled();
      expect(rapportService.addRapportToCollectionIfMissing).toHaveBeenCalledWith(
        rapportCollection,
        ...additionalRapports.map(expect.objectContaining),
      );
      expect(comp.rapportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const testAuto: ITestAuto = { id: 456 };
      const rapport: IRapport = { id: 9749 };
      testAuto.rapport = rapport;

      activatedRoute.data = of({ testAuto });
      comp.ngOnInit();

      expect(comp.rapportsSharedCollection).toContain(rapport);
      expect(comp.testAuto).toEqual(testAuto);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestAuto>>();
      const testAuto = { id: 123 };
      jest.spyOn(testAutoFormService, 'getTestAuto').mockReturnValue(testAuto);
      jest.spyOn(testAutoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testAuto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testAuto }));
      saveSubject.complete();

      // THEN
      expect(testAutoFormService.getTestAuto).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testAutoService.update).toHaveBeenCalledWith(expect.objectContaining(testAuto));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestAuto>>();
      const testAuto = { id: 123 };
      jest.spyOn(testAutoFormService, 'getTestAuto').mockReturnValue({ id: null });
      jest.spyOn(testAutoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testAuto: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testAuto }));
      saveSubject.complete();

      // THEN
      expect(testAutoFormService.getTestAuto).toHaveBeenCalled();
      expect(testAutoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestAuto>>();
      const testAuto = { id: 123 };
      jest.spyOn(testAutoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testAuto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testAutoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRapport', () => {
      it('Should forward to rapportService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(rapportService, 'compareRapport');
        comp.compareRapport(entity, entity2);
        expect(rapportService.compareRapport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
