import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBodega } from '../bodega.model';

@Component({
  standalone: true,
  selector: 'jhi-bodega-detail',
  templateUrl: './bodega-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BodegaDetailComponent {
  bodega = input<IBodega | null>(null);

  previousState(): void {
    window.history.back();
  }
}
