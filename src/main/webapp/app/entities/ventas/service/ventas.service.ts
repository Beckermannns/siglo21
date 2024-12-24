import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVentas, NewVentas } from '../ventas.model';

export type PartialUpdateVentas = Partial<IVentas> & Pick<IVentas, 'id'>;

export type EntityResponseType = HttpResponse<IVentas>;
export type EntityArrayResponseType = HttpResponse<IVentas[]>;

@Injectable({ providedIn: 'root' })
export class VentasService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ventas');

  create(ventas: NewVentas): Observable<EntityResponseType> {
    return this.http.post<IVentas>(this.resourceUrl, ventas, { observe: 'response' });
  }

  update(ventas: IVentas): Observable<EntityResponseType> {
    return this.http.put<IVentas>(`${this.resourceUrl}/${this.getVentasIdentifier(ventas)}`, ventas, { observe: 'response' });
  }

  partialUpdate(ventas: PartialUpdateVentas): Observable<EntityResponseType> {
    return this.http.patch<IVentas>(`${this.resourceUrl}/${this.getVentasIdentifier(ventas)}`, ventas, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVentas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVentas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVentasIdentifier(ventas: Pick<IVentas, 'id'>): number {
    return ventas.id;
  }

  compareVentas(o1: Pick<IVentas, 'id'> | null, o2: Pick<IVentas, 'id'> | null): boolean {
    return o1 && o2 ? this.getVentasIdentifier(o1) === this.getVentasIdentifier(o2) : o1 === o2;
  }

  addVentasToCollectionIfMissing<Type extends Pick<IVentas, 'id'>>(
    ventasCollection: Type[],
    ...ventasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ventas: Type[] = ventasToCheck.filter(isPresent);
    if (ventas.length > 0) {
      const ventasCollectionIdentifiers = ventasCollection.map(ventasItem => this.getVentasIdentifier(ventasItem));
      const ventasToAdd = ventas.filter(ventasItem => {
        const ventasIdentifier = this.getVentasIdentifier(ventasItem);
        if (ventasCollectionIdentifiers.includes(ventasIdentifier)) {
          return false;
        }
        ventasCollectionIdentifiers.push(ventasIdentifier);
        return true;
      });
      return [...ventasToAdd, ...ventasCollection];
    }
    return ventasCollection;
  }
}
