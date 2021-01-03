import { IAppUser } from 'app/shared/model/app-user.model';
import { ExpenseType } from 'app/shared/model/enumerations/expense-type.model';

export interface IExpense {
  id?: number;
  description?: string;
  amount?: string;
  type?: ExpenseType;
  tripId?: number;
  createdById?: number;
  participants?: IAppUser[];
}

export class Expense implements IExpense {
  constructor(
    public id?: number,
    public description?: string,
    public amount?: string,
    public type?: ExpenseType,
    public tripId?: number,
    public createdById?: number,
    public participants?: IAppUser[]
  ) {}
}
