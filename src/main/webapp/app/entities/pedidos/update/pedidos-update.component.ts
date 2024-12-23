import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPedidos } from '../pedidos.model';
import { PedidosService } from '../service/pedidos.service';
import { PedidosFormGroup, PedidosFormService } from './pedidos-form.service';

@Component({
  standalone: true,
  selector: 'jhi-pedidos-update',
  templateUrl: './pedidos-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PedidosUpdateComponent implements OnInit {
  isSaving = false;
  pedidos: IPedidos | null = null;

  protected pedidosService = inject(PedidosService);
  protected pedidosFormService = inject(PedidosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PedidosFormGroup = this.pedidosFormService.createPedidosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedidos }) => {
      this.pedidos = pedidos;
      if (pedidos) {
        this.updateForm(pedidos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pedidos = this.pedidosFormService.getPedidos(this.editForm);
    if (pedidos.id !== null) {
      this.subscribeToSaveResponse(this.pedidosService.update(pedidos));
    } else {
      this.subscribeToSaveResponse(this.pedidosService.create(pedidos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedidos>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pedidos: IPedidos): void {
    this.pedidos = pedidos;
    this.pedidosFormService.resetForm(this.editForm, pedidos);
  }
}
