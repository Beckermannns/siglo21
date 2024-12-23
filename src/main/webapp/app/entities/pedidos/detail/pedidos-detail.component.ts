import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPedidos } from '../pedidos.model';

@Component({
  standalone: true,
  selector: 'jhi-pedidos-detail',
  templateUrl: './pedidos-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PedidosDetailComponent {
  pedidos = input<IPedidos | null>(null);

  previousState(): void {
    window.history.back();
  }
}
