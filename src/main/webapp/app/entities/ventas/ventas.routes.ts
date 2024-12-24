import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VentasResolve from './route/ventas-routing-resolve.service';

const ventasRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ventas.component').then(m => m.VentasComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ventas-detail.component').then(m => m.VentasDetailComponent),
    resolve: {
      ventas: VentasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ventas-update.component').then(m => m.VentasUpdateComponent),
    resolve: {
      ventas: VentasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ventas-update.component').then(m => m.VentasUpdateComponent),
    resolve: {
      ventas: VentasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ventasRoute;
