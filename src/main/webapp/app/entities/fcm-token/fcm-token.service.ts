import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFCMToken } from 'app/shared/model/fcm-token.model';

type EntityResponseType = HttpResponse<IFCMToken>;
type EntityArrayResponseType = HttpResponse<IFCMToken[]>;

@Injectable({ providedIn: 'root' })
export class FCMTokenService {
  public resourceUrl = SERVER_API_URL + 'api/fcm-tokens';

  constructor(protected http: HttpClient) {}

  create(fCMToken: IFCMToken): Observable<EntityResponseType> {
    return this.http.post<IFCMToken>(this.resourceUrl, fCMToken, { observe: 'response' });
  }

  update(fCMToken: IFCMToken): Observable<EntityResponseType> {
    return this.http.put<IFCMToken>(this.resourceUrl, fCMToken, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFCMToken>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFCMToken[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
