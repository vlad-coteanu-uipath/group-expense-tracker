import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExpense, Expense } from 'app/shared/model/expense.model';
import { ExpenseService } from './expense.service';
import { ExpenseComponent } from './expense.component';
import { ExpenseDetailComponent } from './expense-detail.component';
import { ExpenseUpdateComponent } from './expense-update.component';

@Injectable({ providedIn: 'root' })
export class ExpenseResolve implements Resolve<IExpense> {
  constructor(private service: ExpenseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpense> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((expense: HttpResponse<Expense>) => {
          if (expense.body) {
            return of(expense.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Expense());
  }
}

export const expenseRoute: Routes = [
  {
    path: '',
    component: ExpenseComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Expenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpenseDetailComponent,
    resolve: {
      expense: ExpenseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Expenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpenseUpdateComponent,
    resolve: {
      expense: ExpenseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Expenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpenseUpdateComponent,
    resolve: {
      expense: ExpenseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Expenses',
    },
    canActivate: [UserRouteAccessService],
  },
];
