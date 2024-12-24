import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVentas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';

@Component({
  standalone: true,
  templateUrl: './ventas-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VentasDeleteDialogComponent {
  ventas?: IVentas;

  protected ventasService = inject(VentasService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ventasService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
