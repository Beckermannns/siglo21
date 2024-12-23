export interface IBodega {
  id: number;
  producto?: string | null;
  cantidad?: number | null;
  descripcion?: string | null;
}

export type NewBodega = Omit<IBodega, 'id'> & { id: null };
