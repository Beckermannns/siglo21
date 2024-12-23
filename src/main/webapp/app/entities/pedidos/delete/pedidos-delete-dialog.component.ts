import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPedidos } from '../pedidos.model';
import { PedidosService } from '../service/pedidos.service';

@Component({
  standalone: true,
  templateUrl: './pedidos-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PedidosDeleteDialogComponent {
  pedidos?: IPedidos;

  protected pedidosService = inject(PedidosService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pedidosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
