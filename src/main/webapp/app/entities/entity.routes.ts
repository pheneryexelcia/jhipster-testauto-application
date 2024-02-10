import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'rapport',
    data: { pageTitle: 'orekasApp.rapport.home.title' },
    loadChildren: () => import('./rapport/rapport.routes'),
  },
  {
    path: 'test-auto',
    data: { pageTitle: 'orekasApp.testAuto.home.title' },
    loadChildren: () => import('./test-auto/test-auto.routes'),
  },
  {
    path: 'etape-test',
    data: { pageTitle: 'orekasApp.etapeTest.home.title' },
    loadChildren: () => import('./etape-test/etape-test.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
