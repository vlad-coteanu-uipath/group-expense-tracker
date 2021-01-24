import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'trip',
        loadChildren: () => import('./trip/trip.module').then(m => m.GroupExpenseTrackerTripModule),
      },
      {
        path: 'expense',
        loadChildren: () => import('./expense/expense.module').then(m => m.GroupExpenseTrackerExpenseModule),
      },
      {
        path: 'app-user',
        loadChildren: () => import('./app-user/app-user.module').then(m => m.GroupExpenseTrackerAppUserModule),
      },
      {
        path: 'fcm-token',
        loadChildren: () => import('./fcm-token/fcm-token.module').then(m => m.GroupExpenseTrackerFCMTokenModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GroupExpenseTrackerEntityModule {}
