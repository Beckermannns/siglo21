import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVentas } from '../ventas.model';

@Component({
  standalone: true,
  selector: 'jhi-ventas-detail',
  templateUrl: './ventas-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VentasDetailComponent {
  ventas = input<IVentas | null>(null);

  previousState(): void {
    window.history.back();
  }
}
