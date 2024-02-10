import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RapportService } from '../service/rapport.service';
import { IRapport } from '../rapport.model';
import { RapportFormService } from './rapport-form.service';

import { RapportUpdateComponent } from './rapport-update.component';

describe('Rapport Management Update Component', () => {
  let comp: RapportUpdateComponent;
  let fixture: ComponentFixture<RapportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rapportFormService: RapportFormService;
  let rapportService: RapportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RapportUpdateComponent],
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
      .overrideTemplate(RapportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RapportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rapportFormService = TestBed.inject(RapportFormService);
    rapportService = TestBed.inject(RapportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rapport: IRapport = { id: 456 };

      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      expect(comp.rapport).toEqual(rapport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 123 };
      jest.spyOn(rapportFormService, 'getRapport').mockReturnValue(rapport);
      jest.spyOn(rapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rapport }));
      saveSubject.complete();

      // THEN
      expect(rapportFormService.getRapport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rapportService.update).toHaveBeenCalledWith(expect.objectContaining(rapport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 123 };
      jest.spyOn(rapportFormService, 'getRapport').mockReturnValue({ id: null });
      jest.spyOn(rapportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rapport }));
      saveSubject.complete();

      // THEN
      expect(rapportFormService.getRapport).toHaveBeenCalled();
      expect(rapportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 123 };
      jest.spyOn(rapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rapportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
