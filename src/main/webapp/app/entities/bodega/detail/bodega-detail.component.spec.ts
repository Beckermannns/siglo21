import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BodegaDetailComponent } from './bodega-detail.component';

describe('Bodega Management Detail Component', () => {
  let comp: BodegaDetailComponent;
  let fixture: ComponentFixture<BodegaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BodegaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bodega-detail.component').then(m => m.BodegaDetailComponent),
              resolve: { bodega: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BodegaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BodegaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bodega on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BodegaDetailComponent);

      // THEN
      expect(instance.bodega()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
