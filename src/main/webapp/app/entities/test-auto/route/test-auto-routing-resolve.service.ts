import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITestAuto } from '../test-auto.model';
import { TestAutoService } from '../service/test-auto.service';

export const testAutoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITestAuto> => {
  const id = route.params['id'];
  if (id) {
    return inject(TestAutoService)
      .find(id)
      .pipe(
        mergeMap((testAuto: HttpResponse<ITestAuto>) => {
          if (testAuto.body) {
            return of(testAuto.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default testAutoResolve;
