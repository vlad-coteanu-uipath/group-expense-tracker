import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFCMToken } from 'app/shared/model/fcm-token.model';
import { FCMTokenService } from './fcm-token.service';

@Component({
  templateUrl: './fcm-token-delete-dialog.component.html',
})
export class FCMTokenDeleteDialogComponent {
  fCMToken?: IFCMToken;

  constructor(protected fCMTokenService: FCMTokenService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fCMTokenService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fCMTokenListModification');
      this.activeModal.close();
    });
  }
}
