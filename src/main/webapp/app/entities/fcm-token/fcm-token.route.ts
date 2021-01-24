import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFCMToken, FCMToken } from 'app/shared/model/fcm-token.model';
import { FCMTokenService } from './fcm-token.service';
import { FCMTokenComponent } from './fcm-token.component';
import { FCMTokenDetailComponent } from './fcm-token-detail.component';
import { FCMTokenUpdateComponent } from './fcm-token-update.component';

@Injectable({ providedIn: 'root' })
export class FCMTokenResolve implements Resolve<IFCMToken> {
  constructor(private service: FCMTokenService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFCMToken> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fCMToken: HttpResponse<FCMToken>) => {
          if (fCMToken.body) {
            return of(fCMToken.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FCMToken());
  }
}

export const fCMTokenRoute: Routes = [
  {
    path: '',
    component: FCMTokenComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FCMTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FCMTokenDetailComponent,
    resolve: {
      fCMToken: FCMTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FCMTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FCMTokenUpdateComponent,
    resolve: {
      fCMToken: FCMTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FCMTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FCMTokenUpdateComponent,
    resolve: {
      fCMToken: FCMTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FCMTokens',
    },
    canActivate: [UserRouteAccessService],
  },
];
