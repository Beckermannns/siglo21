import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompras } from '../compras.model';
import { ComprasService } from '../service/compras.service';

const comprasResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompras> => {
  const id = route.params.id;
  if (id) {
    return inject(ComprasService)
      .find(id)
      .pipe(
        mergeMap((compras: HttpResponse<ICompras>) => {
          if (compras.body) {
            return of(compras.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default comprasResolve;
