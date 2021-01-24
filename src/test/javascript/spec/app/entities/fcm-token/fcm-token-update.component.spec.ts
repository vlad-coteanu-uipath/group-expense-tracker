import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GroupExpenseTrackerTestModule } from '../../../test.module';
import { FCMTokenUpdateComponent } from 'app/entities/fcm-token/fcm-token-update.component';
import { FCMTokenService } from 'app/entities/fcm-token/fcm-token.service';
import { FCMToken } from 'app/shared/model/fcm-token.model';

describe('Component Tests', () => {
  describe('FCMToken Management Update Component', () => {
    let comp: FCMTokenUpdateComponent;
    let fixture: ComponentFixture<FCMTokenUpdateComponent>;
    let service: FCMTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GroupExpenseTrackerTestModule],
        declarations: [FCMTokenUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FCMTokenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FCMTokenUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FCMTokenService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FCMToken(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FCMToken();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
