import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAppUser, AppUser } from 'app/shared/model/app-user.model';
import { AppUserService } from './app-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from 'app/entities/expense/expense.service';
import { ITrip } from 'app/shared/model/trip.model';
import { TripService } from 'app/entities/trip/trip.service';

type SelectableEntity = IUser | ITrip | IExpense;

type SelectableManyToManyEntity = ITrip | IExpense;

@Component({
  selector: 'jhi-app-user-update',
  templateUrl: './app-user-update.component.html',
})
export class AppUserUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  trips: ITrip[] = [];
  expenses: IExpense[] = [];

  editForm = this.fb.group({
    id: [],
    userId: [],
    trips: [],
    expenses: [],
  });

  constructor(
    protected appUserService: AppUserService,
    protected userService: UserService,
    protected expenseService: ExpenseService,
    protected tripService: TripService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appUser }) => {
      this.updateForm(appUser);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.tripService.query().subscribe((res: HttpResponse<ITrip[]>) => (this.trips = res.body || []));

      this.expenseService.query().subscribe((res: HttpResponse<IExpense[]>) => (this.expenses = res.body || []));
    });
  }

  updateForm(appUser: IAppUser): void {
    this.editForm.patchValue({
      id: appUser.id,
      userId: appUser.userId,
      trips: appUser.trips,
      expenses: appUser.expenses,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appUser = this.createFromForm();
    if (appUser.id !== undefined) {
      this.subscribeToSaveResponse(this.appUserService.update(appUser));
    } else {
      this.subscribeToSaveResponse(this.appUserService.create(appUser));
    }
  }

  private createFromForm(): IAppUser {
    return {
      ...new AppUser(),
      id: this.editForm.get(['id'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      trips: this.editForm.get(['trips'])!.value,
      expenses: this.editForm.get(['expenses'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppUser>>): void {
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

  getSelected(selectedVals: SelectableManyToManyEntity[], option: SelectableManyToManyEntity): SelectableManyToManyEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
