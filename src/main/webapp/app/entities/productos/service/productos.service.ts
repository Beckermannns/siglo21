import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductos, NewProductos } from '../productos.model';

export type PartialUpdateProductos = Partial<IProductos> & Pick<IProductos, 'id'>;

export type EntityResponseType = HttpResponse<IProductos>;
export type EntityArrayResponseType = HttpResponse<IProductos[]>;

@Injectable({ providedIn: 'root' })
export class ProductosService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/productos');

  create(productos: NewProductos): Observable<EntityResponseType> {
    return this.http.post<IProductos>(this.resourceUrl, productos, { observe: 'response' });
  }

  update(productos: IProductos): Observable<EntityResponseType> {
    return this.http.put<IProductos>(`${this.resourceUrl}/${this.getProductosIdentifier(productos)}`, productos, { observe: 'response' });
  }

  partialUpdate(productos: PartialUpdateProductos): Observable<EntityResponseType> {
    return this.http.patch<IProductos>(`${this.resourceUrl}/${this.getProductosIdentifier(productos)}`, productos, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductosIdentifier(productos: Pick<IProductos, 'id'>): number {
    return productos.id;
  }

  compareProductos(o1: Pick<IProductos, 'id'> | null, o2: Pick<IProductos, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductosIdentifier(o1) === this.getProductosIdentifier(o2) : o1 === o2;
  }

  addProductosToCollectionIfMissing<Type extends Pick<IProductos, 'id'>>(
    productosCollection: Type[],
    ...productosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productos: Type[] = productosToCheck.filter(isPresent);
    if (productos.length > 0) {
      const productosCollectionIdentifiers = productosCollection.map(productosItem => this.getProductosIdentifier(productosItem));
      const productosToAdd = productos.filter(productosItem => {
        const productosIdentifier = this.getProductosIdentifier(productosItem);
        if (productosCollectionIdentifiers.includes(productosIdentifier)) {
          return false;
        }
        productosCollectionIdentifiers.push(productosIdentifier);
        return true;
      });
      return [...productosToAdd, ...productosCollection];
    }
    return productosCollection;
  }
}
