import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GroupExpenseTrackerSharedModule } from 'app/shared/shared.module';

import { MetricsComponent } from './metrics.component';

import { metricsRoute } from './metrics.route';

@NgModule({
  imports: [GroupExpenseTrackerSharedModule, RouterModule.forChild([metricsRoute])],
  declarations: [MetricsComponent],
})
export class MetricsModule {}
