import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFCMToken, FCMToken } from 'app/shared/model/fcm-token.model';
import { FCMTokenService } from './fcm-token.service';

@Component({
  selector: 'jhi-fcm-token-update',
  templateUrl: './fcm-token-update.component.html',
})
export class FCMTokenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    token: [],
    appUserId: [],
  });

  constructor(protected fCMTokenService: FCMTokenService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fCMToken }) => {
      this.updateForm(fCMToken);
    });
  }

  updateForm(fCMToken: IFCMToken): void {
    this.editForm.patchValue({
      id: fCMToken.id,
      token: fCMToken.token,
      appUserId: fCMToken.appUserId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fCMToken = this.createFromForm();
    if (fCMToken.id !== undefined) {
      this.subscribeToSaveResponse(this.fCMTokenService.update(fCMToken));
    } else {
      this.subscribeToSaveResponse(this.fCMTokenService.create(fCMToken));
    }
  }

  private createFromForm(): IFCMToken {
    return {
      ...new FCMToken(),
      id: this.editForm.get(['id'])!.value,
      token: this.editForm.get(['token'])!.value,
      appUserId: this.editForm.get(['appUserId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFCMToken>>): void {
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
}
