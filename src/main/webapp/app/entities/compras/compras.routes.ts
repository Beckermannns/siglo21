import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ComprasResolve from './route/compras-routing-resolve.service';

const comprasRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compras.component').then(m => m.ComprasComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compras-detail.component').then(m => m.ComprasDetailComponent),
    resolve: {
      compras: ComprasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compras-update.component').then(m => m.ComprasUpdateComponent),
    resolve: {
      compras: ComprasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compras-update.component').then(m => m.ComprasUpdateComponent),
    resolve: {
      compras: ComprasResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default comprasRoute;
