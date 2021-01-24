import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GroupExpenseTrackerTestModule } from '../../../test.module';
import { FCMTokenComponent } from 'app/entities/fcm-token/fcm-token.component';
import { FCMTokenService } from 'app/entities/fcm-token/fcm-token.service';
import { FCMToken } from 'app/shared/model/fcm-token.model';

describe('Component Tests', () => {
  describe('FCMToken Management Component', () => {
    let comp: FCMTokenComponent;
    let fixture: ComponentFixture<FCMTokenComponent>;
    let service: FCMTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GroupExpenseTrackerTestModule],
        declarations: [FCMTokenComponent],
      })
        .overrideTemplate(FCMTokenComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FCMTokenComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FCMTokenService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FCMToken(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fCMTokens && comp.fCMTokens[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
