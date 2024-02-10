import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EtapeTestService } from '../service/etape-test.service';

import { EtapeTestComponent } from './etape-test.component';

describe('EtapeTest Management Component', () => {
  let comp: EtapeTestComponent;
  let fixture: ComponentFixture<EtapeTestComponent>;
  let service: EtapeTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'etape-test', component: EtapeTestComponent }]),
        HttpClientTestingModule,
        EtapeTestComponent,
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
      .overrideTemplate(EtapeTestComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtapeTestComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EtapeTestService);

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
    expect(comp.etapeTests?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to etapeTestService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getEtapeTestIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getEtapeTestIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
