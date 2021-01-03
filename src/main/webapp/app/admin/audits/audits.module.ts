import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GroupExpenseTrackerSharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [GroupExpenseTrackerSharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent],
})
export class AuditsModule {}
