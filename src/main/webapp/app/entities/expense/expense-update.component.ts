import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IExpense, Expense } from 'app/shared/model/expense.model';
import { ExpenseService } from './expense.service';
import { ITrip } from 'app/shared/model/trip.model';
import { TripService } from 'app/entities/trip/trip.service';
import { IAppUser } from 'app/shared/model/app-user.model';
import { AppUserService } from 'app/entities/app-user/app-user.service';

type SelectableEntity = ITrip | IAppUser;

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  trips: ITrip[] = [];
  appusers: IAppUser[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    amount: [],
    type: [],
    tripId: [],
    createdById: [],
  });

  constructor(
    protected expenseService: ExpenseService,
    protected tripService: TripService,
    protected appUserService: AppUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.updateForm(expense);

      this.tripService.query().subscribe((res: HttpResponse<ITrip[]>) => (this.trips = res.body || []));

      this.appUserService.query().subscribe((res: HttpResponse<IAppUser[]>) => (this.appusers = res.body || []));
    });
  }

  updateForm(expense: IExpense): void {
    this.editForm.patchValue({
      id: expense.id,
      description: expense.description,
      amount: expense.amount,
      type: expense.type,
      tripId: expense.tripId,
      createdById: expense.createdById,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.createFromForm();
    if (expense.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  private createFromForm(): IExpense {
    return {
      ...new Expense(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      type: this.editForm.get(['type'])!.value,
      tripId: this.editForm.get(['tripId'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
