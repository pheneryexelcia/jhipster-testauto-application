import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EtapeTestComponent } from './list/etape-test.component';
import { EtapeTestDetailComponent } from './detail/etape-test-detail.component';
import { EtapeTestUpdateComponent } from './update/etape-test-update.component';
import EtapeTestResolve from './route/etape-test-routing-resolve.service';

const etapeTestRoute: Routes = [
  {
    path: '',
    component: EtapeTestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EtapeTestDetailComponent,
    resolve: {
      etapeTest: EtapeTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EtapeTestUpdateComponent,
    resolve: {
      etapeTest: EtapeTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EtapeTestUpdateComponent,
    resolve: {
      etapeTest: EtapeTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default etapeTestRoute;
