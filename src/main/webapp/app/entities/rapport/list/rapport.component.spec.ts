import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RapportService } from '../service/rapport.service';

import { RapportComponent } from './rapport.component';

describe('Rapport Management Component', () => {
  let comp: RapportComponent;
  let fixture: ComponentFixture<RapportComponent>;
  let service: RapportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'rapport', component: RapportComponent }]),
        HttpClientTestingModule,
        RapportComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(RapportComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RapportComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RapportService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.rapports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to rapportService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getRapportIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRapportIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
