import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompras, NewCompras } from '../compras.model';

export type PartialUpdateCompras = Partial<ICompras> & Pick<ICompras, 'id'>;

export type EntityResponseType = HttpResponse<ICompras>;
export type EntityArrayResponseType = HttpResponse<ICompras[]>;

@Injectable({ providedIn: 'root' })
export class ComprasService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compras');

  create(compras: NewCompras): Observable<EntityResponseType> {
    return this.http.post<ICompras>(this.resourceUrl, compras, { observe: 'response' });
  }

  update(compras: ICompras): Observable<EntityResponseType> {
    return this.http.put<ICompras>(`${this.resourceUrl}/${this.getComprasIdentifier(compras)}`, compras, { observe: 'response' });
  }

  partialUpdate(compras: PartialUpdateCompras): Observable<EntityResponseType> {
    return this.http.patch<ICompras>(`${this.resourceUrl}/${this.getComprasIdentifier(compras)}`, compras, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompras>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompras[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getComprasIdentifier(compras: Pick<ICompras, 'id'>): number {
    return compras.id;
  }

  compareCompras(o1: Pick<ICompras, 'id'> | null, o2: Pick<ICompras, 'id'> | null): boolean {
    return o1 && o2 ? this.getComprasIdentifier(o1) === this.getComprasIdentifier(o2) : o1 === o2;
  }

  addComprasToCollectionIfMissing<Type extends Pick<ICompras, 'id'>>(
    comprasCollection: Type[],
    ...comprasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compras: Type[] = comprasToCheck.filter(isPresent);
    if (compras.length > 0) {
      const comprasCollectionIdentifiers = comprasCollection.map(comprasItem => this.getComprasIdentifier(comprasItem));
      const comprasToAdd = compras.filter(comprasItem => {
        const comprasIdentifier = this.getComprasIdentifier(comprasItem);
        if (comprasCollectionIdentifiers.includes(comprasIdentifier)) {
          return false;
        }
        comprasCollectionIdentifiers.push(comprasIdentifier);
        return true;
      });
      return [...comprasToAdd, ...comprasCollection];
    }
    return comprasCollection;
  }
}
