import { Moment } from 'moment';
import { IExpense } from 'app/shared/model/expense.model';
import { IAppUser } from 'app/shared/model/app-user.model';

export interface ITrip {
  id?: number;
  name?: string;
  description?: string;
  createdDate?: Moment;
  expenses?: IExpense[];
  createdById?: number;
  participants?: IAppUser[];
}

export class Trip implements ITrip {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public createdDate?: Moment,
    public expenses?: IExpense[],
    public createdById?: number,
    public participants?: IAppUser[]
  ) {}
}
