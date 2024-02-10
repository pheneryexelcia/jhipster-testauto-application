import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EtapeTestDetailComponent } from './etape-test-detail.component';

describe('EtapeTest Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EtapeTestDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EtapeTestDetailComponent,
              resolve: { etapeTest: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EtapeTestDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load etapeTest on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EtapeTestDetailComponent);

      // THEN
      expect(instance.etapeTest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
