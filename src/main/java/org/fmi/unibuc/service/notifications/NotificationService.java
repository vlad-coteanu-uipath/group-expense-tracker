package org.fmi.unibuc.service.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.domain.FCMToken;
import org.fmi.unibuc.service.FCMTokenService;
import org.fmi.unibuc.web.rest.AppUserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private static NotificationService INSTANCE = new NotificationService();

    @Autowired
    private FCMTokenService fcmTokenService;

    private NotificationService() {}

    public void sendCreateTripNotification(Long createdById, String createdByDetails, Set<Long> recipientsIds, String tripName) {

        log.info("Sending create trip notification");
        log.info("Trip was created by " + createdByDetails + " and has name: " + tripName);

        String title = "You have been added to " + tripName;
        String descriptionFormat = "%s has added you to a new trip called %s";
        String description = String.format(descriptionFormat, createdByDetails, tripName);

        for(Long recipientId : recipientsIds) {
            Optional<FCMToken> fcmTokenOptional = fcmTokenService.findOneByAppUserId(recipientId);
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
