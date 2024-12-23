import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BodegaResolve from './route/bodega-routing-resolve.service';

const bodegaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bodega.component').then(m => m.BodegaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bodega-detail.component').then(m => m.BodegaDetailComponent),
    resolve: {
      bodega: BodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bodega-update.component').then(m => m.BodegaUpdateComponent),
    resolve: {
      bodega: BodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bodega-update.component').then(m => m.BodegaUpdateComponent),
    resolve: {
      bodega: BodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bodegaRoute;
