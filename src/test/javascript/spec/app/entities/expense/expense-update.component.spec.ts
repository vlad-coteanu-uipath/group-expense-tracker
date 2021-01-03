import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GroupExpenseTrackerTestModule } from '../../../test.module';
import { ExpenseUpdateComponent } from 'app/entities/expense/expense-update.component';
import { ExpenseService } from 'app/entities/expense/expense.service';
import { Expense } from 'app/shared/model/expense.model';

describe('Component Tests', () => {
  describe('Expense Management Update Component', () => {
    let comp: ExpenseUpdateComponent;
    let fixture: ComponentFixture<ExpenseUpdateComponent>;
    let service: ExpenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GroupExpenseTrackerTestModule],
        declarations: [ExpenseUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ExpenseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExpenseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Expense(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Expense();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
