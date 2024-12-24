import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProductosResolve from './route/productos-routing-resolve.service';

const productosRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/productos.component').then(m => m.ProductosComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/productos-detail.component').then(m => m.ProductosDetailComponent),
    resolve: {
      productos: ProductosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/productos-update.component').then(m => m.ProductosUpdateComponent),
    resolve: {
      productos: ProductosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/productos-update.component').then(m => m.ProductosUpdateComponent),
    resolve: {
      productos: ProductosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productosRoute;
