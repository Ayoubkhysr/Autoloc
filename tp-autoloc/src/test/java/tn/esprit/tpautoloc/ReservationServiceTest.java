package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.observer.IReservationObserver;
import tn.esprit.tpautoloc.observer.ReservationSubject;
import tn.esprit.tpautoloc.service.IClientService;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.factory.NotificationFactory;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private ReservationSubject reservationSubject;

    @Autowired
    private IClientService clientService;

    @Autowired
    private TarificationStrategyFactory tarificationStrategyFactory;

    @Autowired
    private NotificationFactory notificationFactory;

    /**
     * Test de création d'une réservation et déclenchement des notifications Observer :
     * [EMAIL] Confirmation envoyee au client ...
     * [SMS] Confirmation envoyee au ...
     */
    @Test
    void testCreerReservationEtNotifierObservateurs() {
        System.out.println("=== TEST OBSERVER PATTERN : Confirmation Réservation ===");

        // 1. Créer un client de test
        Client client = new Client();
        client.setNom("Ben Mahmoud");
        client.setPrenom("Sami");
        client.setEmail("sami.bm@gmail.com");
        client.setTelephone("98123456");
        Client savedClient = clientService.create(client);

        // 2. Créer une réservation via le service
        Reservation reservation = reservationService.creerReservation(savedClient);

        assertNotNull(reservation.getIdReservation(), "L'identifiant de la réservation doit être généré");
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
        assertEquals("sami.bm@gmail.com", reservation.getClient().getEmail());

        System.out.println("Réservation créée avec succès ID : #" + reservation.getIdReservation());

        // Nettoyage
        reservationService.deleteById(reservation.getIdReservation());
        clientService.deleteById(savedClient.getIdClient());
    }

    /**
     * Test d'annulation d'une réservation et déclenchement des notifications Observer :
     * [EMAIL] Annulation notifiee au client ...
     * [SMS] Annulation notifiee au ...
     */
    @Test
    void testAnnulerReservationEtNotifierObservateurs() {
        System.out.println("=== TEST OBSERVER PATTERN : Annulation Réservation ===");

        // 1. Créer un client de test
        Client client = new Client();
        client.setNom("Trabelsi");
        client.setPrenom("Nour");
        client.setEmail("nour.trabelsi@gmail.com");
        client.setTelephone("22334455");
        Client savedClient = clientService.create(client);

        // 2. Créer la réservation
        Reservation reservation = reservationService.creerReservation(savedClient);

        // 3. Annuler la réservation
        Reservation annulee = reservationService.annulerReservation(reservation);

        assertEquals(StatutReservation.ANNULEE, annulee.getStatut());
        System.out.println("Réservation annulée avec succès ID : #" + annulee.getIdReservation());

        // Nettoyage
        reservationService.deleteById(annulee.getIdReservation());
        clientService.deleteById(savedClient.getIdClient());
    }

    /**
     * Test du Strategy Pattern et de la Factory de stratégies de tarification.
     */
    @Test
    void testTarificationStrategyFactory() {
        System.out.println("=== TEST STRATEGY PATTERN & FACTORY ===");

        Vehicule vehicule = new Vehicule();
        vehicule.setTarifJournalier(new BigDecimal("100.00"));

        // Cas 1 : Séjour de 10 jours -> Longue durée (remise de 20% -> 0.80)
        Reservation resaLongue = new Reservation();
        resaLongue.setDateDebut(LocalDate.of(2026, 7, 1));
        resaLongue.setDateFin(LocalDate.of(2026, 7, 11)); // 10 jours
        ITarificationStrategy stratLongue = tarificationStrategyFactory.resoudre(resaLongue);
        assertEquals("LONGUE_DUREE", stratLongue.getCode());
        BigDecimal tarifLongue = stratLongue.calculerTarif(vehicule, resaLongue);
        // 10 jours * 100 * 0.80 = 800.00
        assertEquals(0, new BigDecimal("800.00").compareTo(tarifLongue));
        System.out.println("Tarif longue durée calculé : " + tarifLongue + " DT (code: " + stratLongue.getCode() + ")");

        // Cas 2 : Séjour standard (en semaine, 4 jours du lundi au vendredi)
        Reservation resaStandard = new Reservation();
        resaStandard.setDateDebut(LocalDate.of(2026, 7, 6)); // Lundi
        resaStandard.setDateFin(LocalDate.of(2026, 7, 10)); // Vendredi
        ITarificationStrategy stratStandard = tarificationStrategyFactory.resoudre(resaStandard);
        assertEquals("STANDARD", stratStandard.getCode());
        BigDecimal tarifStandard = stratStandard.calculerTarif(vehicule, resaStandard);
        // 4 jours * 100 = 400.00
        assertEquals(0, new BigDecimal("400.00").compareTo(tarifStandard));
        System.out.println("Tarif standard calculé : " + tarifStandard + " DT (code: " + stratStandard.getCode() + ")");

        // Cas 3 : Séjour week-end (du vendredi au dimanche, 2 jours, remise 10% -> 0.90)
        Reservation resaWeekend = new Reservation();
        resaWeekend.setDateDebut(LocalDate.of(2026, 7, 10)); // Vendredi
        resaWeekend.setDateFin(LocalDate.of(2026, 7, 12)); // Dimanche
        ITarificationStrategy stratWeekend = tarificationStrategyFactory.resoudre(resaWeekend);
        assertEquals("WEEKEND", stratWeekend.getCode());
        BigDecimal tarifWeekend = stratWeekend.calculerTarif(vehicule, resaWeekend);
        // 2 jours * 100 * 0.90 = 180.00
        assertEquals(0, new BigDecimal("180.00").compareTo(tarifWeekend));
        System.out.println("Tarif week-end calculé : " + tarifWeekend + " DT (code: " + stratWeekend.getCode() + ")");
    }

    /**
     * Test de la NotificationFactory (Factory Method).
     */
    @Test
    void testNotificationFactory() {
        System.out.println("=== TEST NOTIFICATION FACTORY ===");

        IReservationObserver emailObserver = notificationFactory.creerObservateur("EMAIL");
        assertNotNull(emailObserver);

        IReservationObserver smsObserver = notificationFactory.creerObservateur("SMS");
        assertNotNull(smsObserver);

        assertThrows(IllegalArgumentException.class, () -> notificationFactory.creerObservateur("PUSH"));
        assertThrows(IllegalArgumentException.class, () -> notificationFactory.creerObservateur(null));

        System.out.println("NotificationFactory validée avec succès pour EMAIL et SMS.");
    }
}
