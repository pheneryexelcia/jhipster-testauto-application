import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TestAutoComponent } from './list/test-auto.component';
import { TestAutoDetailComponent } from './detail/test-auto-detail.component';
import { TestAutoUpdateComponent } from './update/test-auto-update.component';
import TestAutoResolve from './route/test-auto-routing-resolve.service';

const testAutoRoute: Routes = [
  {
    path: '',
    component: TestAutoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TestAutoDetailComponent,
    resolve: {
      testAuto: TestAutoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TestAutoUpdateComponent,
    resolve: {
      testAuto: TestAutoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TestAutoUpdateComponent,
    resolve: {
      testAuto: TestAutoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default testAutoRoute;
