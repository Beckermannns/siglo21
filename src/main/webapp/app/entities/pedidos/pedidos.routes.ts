import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PedidosResolve from './route/pedidos-routing-resolve.service';

const pedidosRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pedidos.component').then(m => m.PedidosComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pedidos-detail.component').then(m => m.PedidosDetailComponent),
    resolve: {
      pedidos: PedidosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pedidos-update.component').then(m => m.PedidosUpdateComponent),
    resolve: {
      pedidos: PedidosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pedidos-update.component').then(m => m.PedidosUpdateComponent),
    resolve: {
      pedidos: PedidosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pedidosRoute;
