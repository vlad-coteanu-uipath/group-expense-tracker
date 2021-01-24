import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFCMToken } from 'app/shared/model/fcm-token.model';
import { FCMTokenService } from './fcm-token.service';
import { FCMTokenDeleteDialogComponent } from './fcm-token-delete-dialog.component';

@Component({
  selector: 'jhi-fcm-token',
  templateUrl: './fcm-token.component.html',
})
export class FCMTokenComponent implements OnInit, OnDestroy {
  fCMTokens?: IFCMToken[];
  eventSubscriber?: Subscription;

  constructor(protected fCMTokenService: FCMTokenService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.fCMTokenService.query().subscribe((res: HttpResponse<IFCMToken[]>) => (this.fCMTokens = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFCMTokens();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFCMToken): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFCMTokens(): void {
    this.eventSubscriber = this.eventManager.subscribe('fCMTokenListModification', () => this.loadAll());
  }

  delete(fCMToken: IFCMToken): void {
    const modalRef = this.modalService.open(FCMTokenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fCMToken = fCMToken;
  }
}
