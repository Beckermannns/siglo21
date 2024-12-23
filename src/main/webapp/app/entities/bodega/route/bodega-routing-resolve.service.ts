import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';

const bodegaResolve = (route: ActivatedRouteSnapshot): Observable<null | IBodega> => {
  const id = route.params.id;
  if (id) {
    return inject(BodegaService)
      .find(id)
      .pipe(
        mergeMap((bodega: HttpResponse<IBodega>) => {
          if (bodega.body) {
            return of(bodega.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bodegaResolve;
