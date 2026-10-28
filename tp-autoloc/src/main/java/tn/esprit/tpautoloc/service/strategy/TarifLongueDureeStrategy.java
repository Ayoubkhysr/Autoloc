package tn.esprit.tpautoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie de tarification longue durée (remise de 20%).
 */
@Component
public class TarifLongueDureeStrategy implements ITarificationStrategy {

    private static final BigDecimal REMISE_LONGUE_DUREE = new BigDecimal("0.80");

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
        return tarifBase.multiply(REMISE_LONGUE_DUREE);
    }

    @Override
    public String getCode() {
        return "LONGUE_DUREE";
    }
}
