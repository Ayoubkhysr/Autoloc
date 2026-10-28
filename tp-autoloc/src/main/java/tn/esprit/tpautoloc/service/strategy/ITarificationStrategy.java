package tn.esprit.tpautoloc.service.strategy;

import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import java.math.BigDecimal;

/**
 * Interface Strategy pour le calcul dynamique du tarif de location.
 */
public interface ITarificationStrategy {
    BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation);
    String getCode(); // Utilisé pour sélectionner dynamiquement la stratégie
}
