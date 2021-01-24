import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GroupExpenseTrackerTestModule } from '../../../test.module';
import { FCMTokenDetailComponent } from 'app/entities/fcm-token/fcm-token-detail.component';
import { FCMToken } from 'app/shared/model/fcm-token.model';

describe('Component Tests', () => {
  describe('FCMToken Management Detail Component', () => {
    let comp: FCMTokenDetailComponent;
    let fixture: ComponentFixture<FCMTokenDetailComponent>;
    const route = ({ data: of({ fCMToken: new FCMToken(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GroupExpenseTrackerTestModule],
        declarations: [FCMTokenDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FCMTokenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FCMTokenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fCMToken on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fCMToken).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
