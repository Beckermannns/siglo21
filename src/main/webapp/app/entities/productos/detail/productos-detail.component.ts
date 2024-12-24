import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProductos } from '../productos.model';

@Component({
  standalone: true,
  selector: 'jhi-productos-detail',
  templateUrl: './productos-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProductosDetailComponent {
  productos = input<IProductos | null>(null);

  previousState(): void {
    window.history.back();
  }
}
