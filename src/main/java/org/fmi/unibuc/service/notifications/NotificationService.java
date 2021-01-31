package org.fmi.unibuc.service.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.fmi.unibuc.domain.FCMToken;
import org.fmi.unibuc.service.FCMTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private FCMTokenService fcmTokenService;

    public void sendAddedToTripNotification(List<Long> recipientsIds, String tripName) {
        log.info("Sending added to existing trip notification");

        String title = "You have been added to the existing trip " + tripName;
        String description = "ou have been added to the existing trip " + tripName;

        sendMessage(recipientsIds, title, description);
    }

    public void sendRemovedFromTripNotification(List<Long> recipientsIds, String tripName) {
        log.info("Sending removed from trip notification");

        String title = "You have been removed from the trip " + tripName;
        String description = "You have been removed from the trip " + tripName;

        sendMessage(recipientsIds, title, description);
    }

    public void sendCreateTripNotification(Long createdById, String createdByDetails, Set<Long> recipientsIds, String tripName) {

        log.info("Sending create trip notification");
        log.info("Trip was created by " + createdByDetails + " with name: " + tripName);

        String title = "You have been added to " + tripName;
        String descriptionFormat = "%s has added you to a new trip called %s";
        String description = String.format(descriptionFormat, createdByDetails, tripName);

        sendMessage(recipientsIds, title, description);
    }

    public void sendCreateExpenseNotification(Long createdById, String createdByDetails, Set<Long> recipientsIds, String tripName, String expenseDesc, String amountPerUser) {

        log.info("Sending create expense notification");
        log.info("Expense was created by " + createdByDetails + " with description: " + expenseDesc);

        String title = "You have been added to a new expense named " + expenseDesc + ".";
        String descriptionFormat = "%s has added you to a new expense inside trip %s, named %s. The amount of money you owe is %s";
        String description = String.format(descriptionFormat, createdByDetails, tripName, expenseDesc, amountPerUser);

        sendMessage(recipientsIds, title, description);
    }

    private void sendMessage(Collection<Long> recipientsIds, String title, String description) {
        for(Long recipientId : recipientsIds) {
            if(recipientId == null) {
                continue;
            }
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

}
