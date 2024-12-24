export interface ICompras {
  id: number;
  detalle?: string | null;
  cantidad?: number | null;
  precio?: number | null;
}

export type NewCompras = Omit<ICompras, 'id'> & { id: null };
