import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GroupExpenseTrackerSharedModule } from 'app/shared/shared.module';
import { ExpenseComponent } from './expense.component';
import { ExpenseDetailComponent } from './expense-detail.component';
import { ExpenseUpdateComponent } from './expense-update.component';
import { ExpenseDeleteDialogComponent } from './expense-delete-dialog.component';
import { expenseRoute } from './expense.route';

@NgModule({
  imports: [GroupExpenseTrackerSharedModule, RouterModule.forChild(expenseRoute)],
  declarations: [ExpenseComponent, ExpenseDetailComponent, ExpenseUpdateComponent, ExpenseDeleteDialogComponent],
  entryComponents: [ExpenseDeleteDialogComponent],
})
export class GroupExpenseTrackerExpenseModule {}
