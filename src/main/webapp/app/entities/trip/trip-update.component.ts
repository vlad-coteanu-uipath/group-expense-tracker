import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITrip, Trip } from 'app/shared/model/trip.model';
import { TripService } from './trip.service';
import { IAppUser } from 'app/shared/model/app-user.model';
import { AppUserService } from 'app/entities/app-user/app-user.service';

@Component({
  selector: 'jhi-trip-update',
  templateUrl: './trip-update.component.html',
})
export class TripUpdateComponent implements OnInit {
  isSaving = false;
  appusers: IAppUser[] = [];
  createdDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    createdDate: [],
    createdById: [],
  });

  constructor(
    protected tripService: TripService,
    protected appUserService: AppUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trip }) => {
      this.updateForm(trip);

      this.appUserService.query().subscribe((res: HttpResponse<IAppUser[]>) => (this.appusers = res.body || []));
    });
  }

  updateForm(trip: ITrip): void {
    this.editForm.patchValue({
      id: trip.id,
      name: trip.name,
      description: trip.description,
      createdDate: trip.createdDate,
      createdById: trip.createdById,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trip = this.createFromForm();
    if (trip.id !== undefined) {
      this.subscribeToSaveResponse(this.tripService.update(trip));
    } else {
      this.subscribeToSaveResponse(this.tripService.create(trip));
    }
  }

  private createFromForm(): ITrip {
    return {
      ...new Trip(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrip>>): void {
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

  trackById(index: number, item: IAppUser): any {
    return item.id;
  }
}
