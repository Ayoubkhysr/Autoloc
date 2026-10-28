package tn.esprit.tpautoloc.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;

/**
 * Observateur concret : notification par email (simulée via logs).
 */
@Slf4j
@Component
public class EmailNotificationObserver implements IReservationObserver {

    @Override
    public void onReservationConfirmee(Reservation reservation) {
        String email = (reservation != null && reservation.getClient() != null)
                ? reservation.getClient().getEmail() : "inconnu";
        Long id = (reservation != null) ? reservation.getIdReservation() : null;
        log.info("[EMAIL] Confirmation envoyee au client {} pour la reservation #{}", email, id);
    }

    @Override
    public void onReservationAnnulee(Reservation reservation) {
        String email = (reservation != null && reservation.getClient() != null)
                ? reservation.getClient().getEmail() : "inconnu";
        Long id = (reservation != null) ? reservation.getIdReservation() : null;
        log.info("[EMAIL] Annulation notifiee au client {} pour la reservation #{}", email, id);
    }
}
