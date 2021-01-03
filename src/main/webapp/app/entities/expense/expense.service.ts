import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExpense } from 'app/shared/model/expense.model';

type EntityResponseType = HttpResponse<IExpense>;
type EntityArrayResponseType = HttpResponse<IExpense[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  public resourceUrl = SERVER_API_URL + 'api/expenses';

  constructor(protected http: HttpClient) {}

  create(expense: IExpense): Observable<EntityResponseType> {
    return this.http.post<IExpense>(this.resourceUrl, expense, { observe: 'response' });
  }

  update(expense: IExpense): Observable<EntityResponseType> {
    return this.http.put<IExpense>(this.resourceUrl, expense, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpense[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
