import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GroupExpenseTrackerSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [GroupExpenseTrackerSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class GroupExpenseTrackerHomeModule {}
