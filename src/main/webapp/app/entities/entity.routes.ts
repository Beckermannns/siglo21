import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'siglo21App.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'pedidos',
    data: { pageTitle: 'siglo21App.pedidos.home.title' },
    loadChildren: () => import('./pedidos/pedidos.routes'),
  },
  {
    path: 'bodega',
    data: { pageTitle: 'siglo21App.bodega.home.title' },
    loadChildren: () => import('./bodega/bodega.routes'),
  },
  {
    path: 'productos',
    data: { pageTitle: 'siglo21App.productos.home.title' },
    loadChildren: () => import('./productos/productos.routes'),
  },
  {
    path: 'compras',
    data: { pageTitle: 'siglo21App.compras.home.title' },
    loadChildren: () => import('./compras/compras.routes'),
  },
  {
    path: 'ventas',
    data: { pageTitle: 'siglo21App.ventas.home.title' },
    loadChildren: () => import('./ventas/ventas.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
