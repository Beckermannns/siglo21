export interface IPedidos {
  id: number;
  descripcion?: string | null;
  estado?: boolean | null;
}

export type NewPedidos = Omit<IPedidos, 'id'> & { id: null };
