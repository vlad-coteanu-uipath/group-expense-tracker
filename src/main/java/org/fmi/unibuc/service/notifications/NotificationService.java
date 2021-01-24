package org.fmi.unibuc.service.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.domain.FCMToken;
import org.fmi.unibuc.service.FCMTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

public class NotificationService {

    private static NotificationService INSTANCE = new NotificationService();

    @Autowired
    private FCMTokenService fcmTokenService;

    private NotificationService() {}

    public void sendCreateTripNotification(AppUser createdBy, Set<AppUser> recipients, String tripName) {

        String title = "You have been added to " + tripName;
        String descriptionFormat = "%s %s (%s) has added you to a new trip called %s";
        String description = String.format(
            descriptionFormat,
            createdBy.getUser().getFirstName(),
            createdBy.getUser().getLastName(),
            createdBy.getUser().getLogin(),
            tripName);

        for(AppUser recipient : recipients) {
            Optional<FCMToken> fcmTokenOptional = fcmTokenService.findOneByAppUserId(recipient.getId());
            if(!fcmTokenOptional.isPresent()) {
                continue;
            }
            FCMToken fcmToken = fcmTokenOptional.get();
            Message message = Message.builder()
                .putData("title", title)
                .putData("description", description)
                .setToken(fcmToken.getToken())
                .build();
            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        }


    }

    public static NotificationService getInstance() {
        return INSTANCE;
    }
}
