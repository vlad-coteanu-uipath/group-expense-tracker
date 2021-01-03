import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITrip } from 'app/shared/model/trip.model';

type EntityResponseType = HttpResponse<ITrip>;
type EntityArrayResponseType = HttpResponse<ITrip[]>;

@Injectable({ providedIn: 'root' })
export class TripService {
  public resourceUrl = SERVER_API_URL + 'api/trips';

  constructor(protected http: HttpClient) {}

  create(trip: ITrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http
      .post<ITrip>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trip: ITrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trip);
    return this.http
      .put<ITrip>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrip>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrip[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(trip: ITrip): ITrip {
    const copy: ITrip = Object.assign({}, trip, {
      createdDate: trip.createdDate && trip.createdDate.isValid() ? trip.createdDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((trip: ITrip) => {
        trip.createdDate = trip.createdDate ? moment(trip.createdDate) : undefined;
      });
    }
    return res;
  }
}
