import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BodegaService } from '../service/bodega.service';
import { IBodega } from '../bodega.model';
import { BodegaFormService } from './bodega-form.service';

import { BodegaUpdateComponent } from './bodega-update.component';

describe('Bodega Management Update Component', () => {
  let comp: BodegaUpdateComponent;
  let fixture: ComponentFixture<BodegaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bodegaFormService: BodegaFormService;
  let bodegaService: BodegaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BodegaUpdateComponent],
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
      .overrideTemplate(BodegaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BodegaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bodegaFormService = TestBed.inject(BodegaFormService);
    bodegaService = TestBed.inject(BodegaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bodega: IBodega = { id: 456 };

      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      expect(comp.bodega).toEqual(bodega);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBodega>>();
      const bodega = { id: 123 };
      jest.spyOn(bodegaFormService, 'getBodega').mockReturnValue(bodega);
      jest.spyOn(bodegaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bodega }));
      saveSubject.complete();

      // THEN
      expect(bodegaFormService.getBodega).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bodegaService.update).toHaveBeenCalledWith(expect.objectContaining(bodega));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBodega>>();
      const bodega = { id: 123 };
      jest.spyOn(bodegaFormService, 'getBodega').mockReturnValue({ id: null });
      jest.spyOn(bodegaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bodega: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bodega }));
      saveSubject.complete();

      // THEN
      expect(bodegaFormService.getBodega).toHaveBeenCalled();
      expect(bodegaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBodega>>();
      const bodega = { id: 123 };
      jest.spyOn(bodegaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bodegaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
