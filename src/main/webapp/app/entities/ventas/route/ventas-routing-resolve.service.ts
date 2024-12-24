import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVentas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';

const ventasResolve = (route: ActivatedRouteSnapshot): Observable<null | IVentas> => {
  const id = route.params.id;
  if (id) {
    return inject(VentasService)
      .find(id)
      .pipe(
        mergeMap((ventas: HttpResponse<IVentas>) => {
          if (ventas.body) {
            return of(ventas.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ventasResolve;
