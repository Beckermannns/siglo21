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
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
