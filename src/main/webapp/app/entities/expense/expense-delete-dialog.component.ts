import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from './expense.service';

@Component({
  templateUrl: './expense-delete-dialog.component.html',
})
export class ExpenseDeleteDialogComponent {
  expense?: IExpense;

  constructor(protected expenseService: ExpenseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('expenseListModification');
      this.activeModal.close();
    });
  }
}
