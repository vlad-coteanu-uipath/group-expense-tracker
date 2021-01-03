import { IExpense } from 'app/shared/model/expense.model';
import { ITrip } from 'app/shared/model/trip.model';

export interface IAppUser {
  id?: number;
  userId?: number;
  createdExpenses?: IExpense[];
  createdTrips?: ITrip[];
  trips?: ITrip[];
  expenses?: IExpense[];
}

export class AppUser implements IAppUser {
  constructor(
    public id?: number,
    public userId?: number,
    public createdExpenses?: IExpense[],
    public createdTrips?: ITrip[],
    public trips?: ITrip[],
    public expenses?: IExpense[]
  ) {}
}
