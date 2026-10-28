package tn.esprit.tpautoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie de tarification week-end (remise de 10%).
 */
@Component
public class TarifWeekendStrategy implements ITarificationStrategy {

    private static final BigDecimal REMISE_WEEKEND = new BigDecimal("0.90");

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
        BigDecimal tarifBase = vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(nbJours));
        return tarifBase.multiply(REMISE_WEEKEND);
    }

    @Override
    public String getCode() {
        return "WEEKEND";
    }
}
