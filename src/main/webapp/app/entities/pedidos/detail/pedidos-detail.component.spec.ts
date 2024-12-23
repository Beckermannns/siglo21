import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PedidosDetailComponent } from './pedidos-detail.component';

describe('Pedidos Management Detail Component', () => {
  let comp: PedidosDetailComponent;
  let fixture: ComponentFixture<PedidosDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidosDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./pedidos-detail.component').then(m => m.PedidosDetailComponent),
              resolve: { pedidos: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PedidosDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PedidosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pedidos on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PedidosDetailComponent);

      // THEN
      expect(instance.pedidos()).toEqual(expect.objectContaining({ id: 123 }));
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
