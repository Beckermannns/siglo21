import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICompras } from '../compras.model';
import { ComprasService } from '../service/compras.service';

@Component({
  standalone: true,
  templateUrl: './compras-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ComprasDeleteDialogComponent {
  compras?: ICompras;

  protected comprasService = inject(ComprasService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.comprasService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
