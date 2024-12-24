import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICompras } from '../compras.model';

@Component({
  standalone: true,
  selector: 'jhi-compras-detail',
  templateUrl: './compras-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ComprasDetailComponent {
  compras = input<ICompras | null>(null);

  previousState(): void {
    window.history.back();
  }
}
