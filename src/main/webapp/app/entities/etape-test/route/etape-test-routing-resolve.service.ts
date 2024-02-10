import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEtapeTest } from '../etape-test.model';
import { EtapeTestService } from '../service/etape-test.service';

export const etapeTestResolve = (route: ActivatedRouteSnapshot): Observable<null | IEtapeTest> => {
  const id = route.params['id'];
  if (id) {
    return inject(EtapeTestService)
      .find(id)
      .pipe(
        mergeMap((etapeTest: HttpResponse<IEtapeTest>) => {
          if (etapeTest.body) {
            return of(etapeTest.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default etapeTestResolve;
