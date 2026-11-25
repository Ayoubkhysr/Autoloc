package tn.esprit.tpautoloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifLongueDureeStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifStandardStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifWeekendStrategy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Atelier 6 - Partie 5 : Tests unitaires de la TarificationStrategyFactory.
 * Conforme aux spécifications des pages 12 et 13 du sujet de TP.
 */
class TarificationStrategyFactoryTest {

    private TarificationStrategyFactory factory;
    private TarifStandardStrategy standard;
    private TarifWeekendStrategy weekend;
    private TarifLongueDureeStrategy longueDuree;

    @BeforeEach
    void setUp() {
        standard = new TarifStandardStrategy();
        weekend = new TarifWeekendStrategy();
        longueDuree = new TarifLongueDureeStrategy();
        factory = new TarificationStrategyFactory(List.of(standard, weekend, longueDuree));
    }

    /**
     * Test : longue durée (≥ 7 jours)
     * dateDebut = 2024-01-01, dateFin = 2024-01-08 (7 jours)
     */
    @Test
    @DisplayName("Doit retourner LONGUE_DUREE pour un séjour de 7 jours ou plus")
    void doitRetournerLongueDureePour7JoursOuPlus() {
        // 1. Construire une réservation avec 7 jours de différence
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 1, 1));
        reservation.setDateFin(LocalDate.of(2024, 1, 8));

        // 2. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 3. Vérifier les deux assertions
        assertThat(resultat).isInstanceOf(TarifLongueDureeStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    /**
     * Test : borne exacte à 7 jours
     * dateDebut = 2024-05-01, dateFin = 2024-05-08 (exactement 7 jours)
     */
    @Test
    @DisplayName("Doit retourner LONGUE_DUREE pour exactement 7 jours (borne)")
    void doitRetournerLongueDureePourExactement7Jours() {
        // 1. Construire une réservation du 2024-05-01 au 2024-05-08
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 5, 1));
        reservation.setDateFin(LocalDate.of(2024, 5, 8));

        // 2. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 3. Vérifier que le code vaut LONGUE_DUREE
        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    /**
     * Test : court séjour commençant un vendredi
     * dateDebut = 2024-03-08 (vendredi), dateFin = 2024-03-10 (dimanche) -> 2 jours (≤ 3)
     */
    @Test
    @DisplayName("Doit retourner WEEKEND pour un court séjour (≤ 3 jours) commençant un vendredi")
    void doitRetournerWeekendPourCourtSejourVendredi() {
        // 1. Choisir une date de début un vendredi
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 8)); // Vendredi
        reservation.setDateFin(LocalDate.of(2024, 3, 10)); // Dimanche (2 jours)

        // 2. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 3. Vérifier que le type est TarifWeekendStrategy et le code est WEEKEND
        assertThat(resultat).isInstanceOf(TarifWeekendStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("WEEKEND");
    }

    /**
     * Test : durée moyenne (4 à 6 jours)
     * dateDebut = 2024-03-04 (lundi), dateFin = 2024-03-08 (vendredi) -> 4 jours
     */
    @Test
    @DisplayName("Doit retourner STANDARD pour une durée moyenne (4 à 6 jours)")
    void doitRetournerStandardPourMoyenneDuree() {
        // 1. Construire une réservation du 2024-03-04 (lundi) au 2024-03-08 -> 4 jours
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 4));
        reservation.setDateFin(LocalDate.of(2024, 3, 8));

        // 2. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 3. Vérifier que le code vaut STANDARD
        assertThat(resultat.getCode()).isEqualTo("STANDARD");
    }
}
