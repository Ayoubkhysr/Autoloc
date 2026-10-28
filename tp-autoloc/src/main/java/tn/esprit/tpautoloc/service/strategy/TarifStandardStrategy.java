package tn.esprit.tpautoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie de tarification standard sans remise particulière.
 */
@Component
public class TarifStandardStrategy implements ITarificationStrategy {

    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        if (vehicule == null || vehicule.getTarifJournalier() == null) {
            return BigDecimal.ZERO;
        }
        long nbJours = 1;
        if (reservation != null && reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            nbJours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
            if (nbJours <= 0) {
                nbJours = 1;
            }
        }
        return vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(nbJours));
    }

    @Override
    public String getCode() {
        return "STANDARD";
    }
}
