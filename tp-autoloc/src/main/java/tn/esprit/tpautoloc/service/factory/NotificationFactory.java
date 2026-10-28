package tn.esprit.tpautoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.observer.EmailNotificationObserver;
import tn.esprit.tpautoloc.observer.IReservationObserver;
import tn.esprit.tpautoloc.observer.SmsNotificationObserver;

/**
 * Patron GoF : FACTORY METHOD appliquée aux observateurs de notification.
 * Permet d'obtenir le bon canal de notification (email, sms, ...) sans exposer
 * les classes concrètes au code appelant.
 */
@Component
public class NotificationFactory {

    private final EmailNotificationObserver emailNotificationObserver;
    private final SmsNotificationObserver smsNotificationObserver;

    public NotificationFactory(EmailNotificationObserver emailNotificationObserver,
                               SmsNotificationObserver smsNotificationObserver) {
        this.emailNotificationObserver = emailNotificationObserver;
        this.smsNotificationObserver = smsNotificationObserver;
    }

    public IReservationObserver creerObservateur(String canal) {
        if (canal == null) {
            throw new IllegalArgumentException("Le canal de notification ne peut pas être null");
        }
        if ("EMAIL".equalsIgnoreCase(canal)) {
            return emailNotificationObserver;
        } else if ("SMS".equalsIgnoreCase(canal)) {
            return smsNotificationObserver;
        } else {
            throw new IllegalArgumentException("Canal de notification inconnu : " + canal);
        }
    }
}
