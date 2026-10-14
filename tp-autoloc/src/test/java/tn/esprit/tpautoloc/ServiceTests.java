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
import java.util.List;

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
    void testAgenceService() {
        // • Créer une instance d’Agence avec les attributs disponibles dans votre entité
        Agence agence = new Agence();
        agence.setNom("AutoLoc Paris Centre");
        agence.setVille("Paris");
        agence.setAdresse("15 Avenue des Champs-Élysées");
        agence.setTelephone("0145678910");

        // • Enregistrer l’agence
        Agence savedAgence = agenceService.create(agence);

        // • Afficher l’objet retourné
        System.out.println("Agence enregistrée : " + savedAgence.getNom() + " - ID: " + savedAgence.getIdAgence());
        assertNotNull(savedAgence.getIdAgence(), "L'ID ne doit pas être null après l'enregistrement");

        // • Afficher toutes les agences avec findAll()
        List<Agence> agences = agenceService.findAll();
        System.out.println("Toutes les agences (findAll), total : " + agences.size());
        agences.forEach(a -> System.out.println(" - " + a.getIdAgence() + " : " + a.getNom()));

        // • Récupérer une agence avec findById() et afficher le résultat
        Agence retrievedAgence = agenceService.findById(savedAgence.getIdAgence());
        assertNotNull(retrievedAgence);
        System.out.println("Agence récupérée (findById) : " + retrievedAgence.getNom() + " - Ville : " + retrievedAgence.getVille());
        assertEquals("AutoLoc Paris Centre", retrievedAgence.getNom());

        // • Afficher le nombre d’agences avec count()
        System.out.println("Nombre d'agences avec count() : " + agenceService.count());

        // • Supprimer l’agence avec deleteById(), puis vérifier qu’elle n’existe plus
        agenceService.deleteById(savedAgence.getIdAgence());
        assertThrows(ResourceNotFoundException.class, () -> agenceService.findById(savedAgence.getIdAgence()));
        System.out.println("Agence supprimée avec succès.");
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
