import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProductos } from '../productos.model';
import { ProductosService } from '../service/productos.service';

@Component({
  standalone: true,
  templateUrl: './productos-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductosDeleteDialogComponent {
  productos?: IProductos;

  protected productosService = inject(ProductosService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
