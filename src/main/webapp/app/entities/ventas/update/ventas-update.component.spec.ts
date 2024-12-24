import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VentasService } from '../service/ventas.service';
import { IVentas } from '../ventas.model';
import { VentasFormService } from './ventas-form.service';

import { VentasUpdateComponent } from './ventas-update.component';

describe('Ventas Management Update Component', () => {
  let comp: VentasUpdateComponent;
  let fixture: ComponentFixture<VentasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ventasFormService: VentasFormService;
  let ventasService: VentasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VentasUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VentasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VentasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ventasFormService = TestBed.inject(VentasFormService);
    ventasService = TestBed.inject(VentasService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ventas: IVentas = { id: 456 };

      activatedRoute.data = of({ ventas });
      comp.ngOnInit();

      expect(comp.ventas).toEqual(ventas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVentas>>();
      const ventas = { id: 123 };
      jest.spyOn(ventasFormService, 'getVentas').mockReturnValue(ventas);
      jest.spyOn(ventasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ventas }));
      saveSubject.complete();

      // THEN
      expect(ventasFormService.getVentas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ventasService.update).toHaveBeenCalledWith(expect.objectContaining(ventas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVentas>>();
      const ventas = { id: 123 };
      jest.spyOn(ventasFormService, 'getVentas').mockReturnValue({ id: null });
      jest.spyOn(ventasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ventas }));
      saveSubject.complete();

      // THEN
      expect(ventasFormService.getVentas).toHaveBeenCalled();
      expect(ventasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVentas>>();
      const ventas = { id: 123 };
      jest.spyOn(ventasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ventasService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
