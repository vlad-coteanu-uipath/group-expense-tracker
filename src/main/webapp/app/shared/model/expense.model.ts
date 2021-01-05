import { IAppUser } from 'app/shared/model/app-user.model';
import { ExpenseType } from 'app/shared/model/enumerations/expense-type.model';

export interface IExpense {
  id?: number;
  description?: string;
  amount?: number;
  type?: ExpenseType;
  tripId?: number;
  createdById?: number;
  participants?: IAppUser[];
}

export class Expense implements IExpense {
  constructor(
    public id?: number,
    public description?: string,
    public amount?: number,
    public type?: ExpenseType,
    public tripId?: number,
    public createdById?: number,
    public participants?: IAppUser[]
  ) {}
}
