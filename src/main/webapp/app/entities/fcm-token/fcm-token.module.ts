import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GroupExpenseTrackerSharedModule } from 'app/shared/shared.module';
import { FCMTokenComponent } from './fcm-token.component';
import { FCMTokenDetailComponent } from './fcm-token-detail.component';
import { FCMTokenUpdateComponent } from './fcm-token-update.component';
import { FCMTokenDeleteDialogComponent } from './fcm-token-delete-dialog.component';
import { fCMTokenRoute } from './fcm-token.route';

@NgModule({
  imports: [GroupExpenseTrackerSharedModule, RouterModule.forChild(fCMTokenRoute)],
  declarations: [FCMTokenComponent, FCMTokenDetailComponent, FCMTokenUpdateComponent, FCMTokenDeleteDialogComponent],
  entryComponents: [FCMTokenDeleteDialogComponent],
})
export class GroupExpenseTrackerFCMTokenModule {}
