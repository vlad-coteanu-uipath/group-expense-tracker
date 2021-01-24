import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFCMToken } from 'app/shared/model/fcm-token.model';

@Component({
  selector: 'jhi-fcm-token-detail',
  templateUrl: './fcm-token-detail.component.html',
})
export class FCMTokenDetailComponent implements OnInit {
  fCMToken: IFCMToken | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fCMToken }) => (this.fCMToken = fCMToken));
  }

  previousState(): void {
    window.history.back();
  }
}
