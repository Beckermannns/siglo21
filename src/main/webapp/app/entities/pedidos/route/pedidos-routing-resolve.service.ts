import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPedidos } from '../pedidos.model';
import { PedidosService } from '../service/pedidos.service';

const pedidosResolve = (route: ActivatedRouteSnapshot): Observable<null | IPedidos> => {
  const id = route.params.id;
  if (id) {
    return inject(PedidosService)
      .find(id)
      .pipe(
        mergeMap((pedidos: HttpResponse<IPedidos>) => {
          if (pedidos.body) {
            return of(pedidos.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default pedidosResolve;
