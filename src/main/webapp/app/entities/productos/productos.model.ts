export interface IProductos {
  id: number;
  nombre?: string | null;
  cantidad?: number | null;
  precio?: number | null;
}

export type NewProductos = Omit<IProductos, 'id'> & { id: null };
