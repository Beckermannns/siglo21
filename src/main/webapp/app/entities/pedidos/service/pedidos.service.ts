import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPedidos, NewPedidos } from '../pedidos.model';

export type PartialUpdatePedidos = Partial<IPedidos> & Pick<IPedidos, 'id'>;

export type EntityResponseType = HttpResponse<IPedidos>;
export type EntityArrayResponseType = HttpResponse<IPedidos[]>;

@Injectable({ providedIn: 'root' })
export class PedidosService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pedidos');

  create(pedidos: NewPedidos): Observable<EntityResponseType> {
    return this.http.post<IPedidos>(this.resourceUrl, pedidos, { observe: 'response' });
  }

  update(pedidos: IPedidos): Observable<EntityResponseType> {
    return this.http.put<IPedidos>(`${this.resourceUrl}/${this.getPedidosIdentifier(pedidos)}`, pedidos, { observe: 'response' });
  }

  partialUpdate(pedidos: PartialUpdatePedidos): Observable<EntityResponseType> {
    return this.http.patch<IPedidos>(`${this.resourceUrl}/${this.getPedidosIdentifier(pedidos)}`, pedidos, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPedidos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPedidos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPedidosIdentifier(pedidos: Pick<IPedidos, 'id'>): number {
    return pedidos.id;
  }

  comparePedidos(o1: Pick<IPedidos, 'id'> | null, o2: Pick<IPedidos, 'id'> | null): boolean {
    return o1 && o2 ? this.getPedidosIdentifier(o1) === this.getPedidosIdentifier(o2) : o1 === o2;
  }

  addPedidosToCollectionIfMissing<Type extends Pick<IPedidos, 'id'>>(
    pedidosCollection: Type[],
    ...pedidosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pedidos: Type[] = pedidosToCheck.filter(isPresent);
    if (pedidos.length > 0) {
      const pedidosCollectionIdentifiers = pedidosCollection.map(pedidosItem => this.getPedidosIdentifier(pedidosItem));
      const pedidosToAdd = pedidos.filter(pedidosItem => {
        const pedidosIdentifier = this.getPedidosIdentifier(pedidosItem);
        if (pedidosCollectionIdentifiers.includes(pedidosIdentifier)) {
          return false;
        }
        pedidosCollectionIdentifiers.push(pedidosIdentifier);
        return true;
      });
      return [...pedidosToAdd, ...pedidosCollection];
    }
    return pedidosCollection;
  }
}
