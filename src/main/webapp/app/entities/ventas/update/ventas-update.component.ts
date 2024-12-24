import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVentas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';
import { VentasFormGroup, VentasFormService } from './ventas-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ventas-update',
  templateUrl: './ventas-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VentasUpdateComponent implements OnInit {
  isSaving = false;
  ventas: IVentas | null = null;

  protected ventasService = inject(VentasService);
  protected ventasFormService = inject(VentasFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VentasFormGroup = this.ventasFormService.createVentasFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventas }) => {
      this.ventas = ventas;
      if (ventas) {
        this.updateForm(ventas);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ventas = this.ventasFormService.getVentas(this.editForm);
    if (ventas.id !== null) {
      this.subscribeToSaveResponse(this.ventasService.update(ventas));
    } else {
      this.subscribeToSaveResponse(this.ventasService.create(ventas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVentas>>): void {
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

  protected updateForm(ventas: IVentas): void {
    this.ventas = ventas;
    this.ventasFormService.resetForm(this.editForm, ventas);
  }
}
