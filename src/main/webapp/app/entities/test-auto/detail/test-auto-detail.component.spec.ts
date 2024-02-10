import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TestAutoDetailComponent } from './test-auto-detail.component';

describe('TestAuto Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestAutoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TestAutoDetailComponent,
              resolve: { testAuto: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TestAutoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load testAuto on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TestAutoDetailComponent);

      // THEN
      expect(instance.testAuto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
