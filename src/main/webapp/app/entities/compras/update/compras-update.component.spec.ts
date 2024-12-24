import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ComprasService } from '../service/compras.service';
import { ICompras } from '../compras.model';
import { ComprasFormService } from './compras-form.service';

import { ComprasUpdateComponent } from './compras-update.component';

describe('Compras Management Update Component', () => {
  let comp: ComprasUpdateComponent;
  let fixture: ComponentFixture<ComprasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let comprasFormService: ComprasFormService;
  let comprasService: ComprasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ComprasUpdateComponent],
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
      .overrideTemplate(ComprasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComprasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    comprasFormService = TestBed.inject(ComprasFormService);
    comprasService = TestBed.inject(ComprasService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const compras: ICompras = { id: 456 };

      activatedRoute.data = of({ compras });
      comp.ngOnInit();

      expect(comp.compras).toEqual(compras);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompras>>();
      const compras = { id: 123 };
      jest.spyOn(comprasFormService, 'getCompras').mockReturnValue(compras);
      jest.spyOn(comprasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compras });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compras }));
      saveSubject.complete();

      // THEN
      expect(comprasFormService.getCompras).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(comprasService.update).toHaveBeenCalledWith(expect.objectContaining(compras));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompras>>();
      const compras = { id: 123 };
      jest.spyOn(comprasFormService, 'getCompras').mockReturnValue({ id: null });
      jest.spyOn(comprasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compras: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compras }));
      saveSubject.complete();

      // THEN
      expect(comprasFormService.getCompras).toHaveBeenCalled();
      expect(comprasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompras>>();
      const compras = { id: 123 };
      jest.spyOn(comprasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compras });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(comprasService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
