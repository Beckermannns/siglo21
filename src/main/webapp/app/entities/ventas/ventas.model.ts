export interface IVentas {
  id: number;
  descripcion?: string | null;
  cantidad?: number | null;
  total?: number | null;
}

export type NewVentas = Omit<IVentas, 'id'> & { id: null };
