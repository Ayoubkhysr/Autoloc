package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTests {

    @Autowired
    private IContratService contratService;

    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEquipementService equipementService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private IPaiementService paiementService;

    @Autowired
    private IMaintenanceService maintenanceService;

    @Test
    void testContratServiceValidationAndNotFound() {
        Contrat contratWithId = new Contrat();
        contratWithId.setIdContrat(99L);
        assertThrows(IllegalArgumentException.class, () -> contratService.create(contratWithId));

        Contrat contratNegatif = new Contrat();
        contratNegatif.setMontantTotal(new BigDecimal("-10.00"));
        assertThrows(IllegalArgumentException.class, () -> contratService.create(contratNegatif));

        assertThrows(ResourceNotFoundException.class, () -> contratService.findById(999999L));
        assertThrows(ResourceNotFoundException.class, () -> contratService.deleteById(999999L));
    }

    @Test
    void testAgenceServiceValidation() {
        Agence agenceSansNom = new Agence();
        assertThrows(IllegalArgumentException.class, () -> agenceService.create(agenceSansNom));
    }

    @Test
    void testEmployeServiceValidation() {
        Employe employeSansNom = new Employe();
        employeSansNom.setRole(RoleEmploye.AGENT);
        assertThrows(IllegalArgumentException.class, () -> employeService.create(employeSansNom));

        Employe employeSansRole = new Employe();
        employeSansRole.setNom("Dupont");
        assertThrows(IllegalArgumentException.class, () -> employeService.create(employeSansRole));
    }

    @Test
    void testVehiculeServiceValidation() {
        Vehicule vehiculeTarifNegatif = new Vehicule();
        vehiculeTarifNegatif.setTarifJournalier(new BigDecimal("-20.00"));
        assertThrows(IllegalArgumentException.class, () -> vehiculeService.create(vehiculeTarifNegatif));
    }

    @Test
    void testEquipementServiceValidation() {
        Equipement equipementSansLibelle = new Equipement();
        assertThrows(IllegalArgumentException.class, () -> equipementService.create(equipementSansLibelle));
    }

    @Test
    void testClientServiceValidation() {
        Client clientSansNom = new Client();
        clientSansNom.setEmail("test@email.com");
        assertThrows(IllegalArgumentException.class, () -> clientService.create(clientSansNom));

        Client clientSansEmail = new Client();
        clientSansEmail.setNom("Dupont");
        assertThrows(IllegalArgumentException.class, () -> clientService.create(clientSansEmail));
    }

    @Test
    void testReservationServiceValidation() {
        Reservation reservationInvalide = new Reservation();
        reservationInvalide.setDateDebut(LocalDate.of(2026, 6, 10));
        reservationInvalide.setDateFin(LocalDate.of(2026, 6, 5));
        assertThrows(IllegalArgumentException.class, () -> reservationService.create(reservationInvalide));
    }

    @Test
    void testMaintenanceServiceValidation() {
        Maintenance maintenanceInvalide = new Maintenance();
        maintenanceInvalide.setDateDebut(LocalDate.of(2026, 6, 10));
        maintenanceInvalide.setDateFin(LocalDate.of(2026, 6, 5));
        assertThrows(IllegalArgumentException.class, () -> maintenanceService.create(maintenanceInvalide));
    }

    @Test
    void testPaiementServiceNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> paiementService.findById(999999L));
    }
}
