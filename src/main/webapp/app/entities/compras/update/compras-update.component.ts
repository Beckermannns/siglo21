import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompras } from '../compras.model';
import { ComprasService } from '../service/compras.service';
import { ComprasFormGroup, ComprasFormService } from './compras-form.service';

@Component({
  standalone: true,
  selector: 'jhi-compras-update',
  templateUrl: './compras-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ComprasUpdateComponent implements OnInit {
  isSaving = false;
  compras: ICompras | null = null;

  protected comprasService = inject(ComprasService);
  protected comprasFormService = inject(ComprasFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ComprasFormGroup = this.comprasFormService.createComprasFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compras }) => {
      this.compras = compras;
      if (compras) {
        this.updateForm(compras);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compras = this.comprasFormService.getCompras(this.editForm);
    if (compras.id !== null) {
      this.subscribeToSaveResponse(this.comprasService.update(compras));
    } else {
      this.subscribeToSaveResponse(this.comprasService.create(compras));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompras>>): void {
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

  protected updateForm(compras: ICompras): void {
    this.compras = compras;
    this.comprasFormService.resetForm(this.editForm, compras);
  }
}
