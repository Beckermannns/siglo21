import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';
import { BodegaFormGroup, BodegaFormService } from './bodega-form.service';

@Component({
  standalone: true,
  selector: 'jhi-bodega-update',
  templateUrl: './bodega-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BodegaUpdateComponent implements OnInit {
  isSaving = false;
  bodega: IBodega | null = null;

  protected bodegaService = inject(BodegaService);
  protected bodegaFormService = inject(BodegaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BodegaFormGroup = this.bodegaFormService.createBodegaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bodega }) => {
      this.bodega = bodega;
      if (bodega) {
        this.updateForm(bodega);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bodega = this.bodegaFormService.getBodega(this.editForm);
    if (bodega.id !== null) {
      this.subscribeToSaveResponse(this.bodegaService.update(bodega));
    } else {
      this.subscribeToSaveResponse(this.bodegaService.create(bodega));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBodega>>): void {
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

  protected updateForm(bodega: IBodega): void {
    this.bodega = bodega;
    this.bodegaFormService.resetForm(this.editForm, bodega);
  }
}
