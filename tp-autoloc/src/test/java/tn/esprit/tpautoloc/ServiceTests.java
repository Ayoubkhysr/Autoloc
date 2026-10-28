package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
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

    // =========================================================================
    // Partie 2 – Test guidé de AgenceService
    // =========================================================================
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
        System.out.println("=== TEST AgenceService ===");
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

        // • Vérifier l'existence avec existsById()
        assertTrue(agenceService.existsById(savedAgence.getIdAgence()));

        // • Afficher le nombre d’agences avec count()
        System.out.println("Nombre d'agences avec count() : " + agenceService.count());

        // • Supprimer l’agence avec deleteById(), puis vérifier qu’elle n’existe plus
        agenceService.deleteById(savedAgence.getIdAgence());
        assertFalse(agenceService.existsById(savedAgence.getIdAgence()));
        assertThrows(ResourceNotFoundException.class, () -> agenceService.findById(savedAgence.getIdAgence()));
        System.out.println("Agence supprimée avec succès.");
    }

    // =========================================================================
    // Partie 3 – Test guidé de ClientService
    // =========================================================================
    @Test
    void testClientService() {
        System.out.println("=== TEST ClientService ===");
        // • Créer deux clients en utilisant les attributs réellement présents dans l’entité Client
        Client client1 = new Client();
        client1.setNom("Dupont");
        client1.setPrenom("Jean");
        client1.setEmail("jean.dupont@test.com");
        client1.setTelephone("0611223344");
        client1.setNumPermis("B12345678");
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Martin");
        client2.setPrenom("Claire");
        client2.setEmail("claire.martin@test.com");
        client2.setTelephone("0655667788");
        client2.setNumPermis("B87654321");
        client2.setDateInscription(LocalDate.now());

        // • Enregistrer les deux clients
        Client savedClient1 = clientService.create(client1);
        Client savedClient2 = clientService.create(client2);
        assertNotNull(savedClient1.getIdClient(), "Client 1 ID ne doit pas être null");
        assertNotNull(savedClient2.getIdClient(), "Client 2 ID ne doit pas être null");
        System.out.println("Client 1 créé : " + savedClient1.getNom() + " " + savedClient1.getPrenom() + " (ID: " + savedClient1.getIdClient() + ")");
        System.out.println("Client 2 créé : " + savedClient2.getNom() + " " + savedClient2.getPrenom() + " (ID: " + savedClient2.getIdClient() + ")");

        // • Afficher la liste avec findAll()
        List<Client> clients = clientService.findAll();
        System.out.println("Liste des clients (findAll), total : " + clients.size());
        clients.forEach(c -> System.out.println(" - " + c.getIdClient() + " : " + c.getNom() + " " + c.getPrenom() + " (" + c.getEmail() + ")"));
        assertTrue(clients.size() >= 2);

        // • Rechercher un client avec findById()
        Client retrievedClient = clientService.findById(savedClient1.getIdClient());
        assertNotNull(retrievedClient);
        assertEquals("Dupont", retrievedClient.getNom());
        System.out.println("Client récupéré (findById) : " + retrievedClient.getNom() + " " + retrievedClient.getPrenom() + " - " + retrievedClient.getEmail());

        // • Afficher le nombre de clients avec count()
        long totalClients = clientService.count();
        System.out.println("Nombre total de clients (count) : " + totalClients);
        assertTrue(totalClients >= 2);

        // • Supprimer le client avec deleteById(), puis vérifier qu’il n'existe plus
        clientService.deleteById(savedClient1.getIdClient());
        assertFalse(clientService.existsById(savedClient1.getIdClient()));
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(savedClient1.getIdClient()));
        System.out.println("Client 1 supprimé et vérifié avec succès.");

        clientService.deleteById(savedClient2.getIdClient());
        assertFalse(clientService.existsById(savedClient2.getIdClient()));
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(savedClient2.getIdClient()));
        System.out.println("Client 2 supprimé et vérifié avec succès.");
    }

    // =========================================================================
    // Partie 4 – Test guidé de VehiculeService
    // =========================================================================
    @Test
    void testVehiculeService() {
        System.out.println("=== TEST VehiculeService ===");
        // • Créer un véhicule avec les attributs de votre entité Vehicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TN-555-ZZ");
        vehicule.setMarque("Peugeot");
        vehicule.setModele("3008");
        vehicule.setCategorie(CategorieVehicule.SUV);
        vehicule.setTarifJournalier(new BigDecimal("95.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        // • Enregistrer le vehicule
        Vehicule savedVehicule = vehiculeService.create(vehicule);
        assertNotNull(savedVehicule.getIdVehicule(), "ID véhicule généré ne doit pas être null");
        System.out.println("Véhicule enregistré : " + savedVehicule.getMarque() + " " + savedVehicule.getModele() + " - ID: " + savedVehicule.getIdVehicule());

        // • Afficher tous les véhicules avec findAll()
        List<Vehicule> vehicules = vehiculeService.findAll();
        System.out.println("Tous les véhicules (findAll), total : " + vehicules.size());
        vehicules.forEach(v -> System.out.println(" - " + v.getIdVehicule() + " : " + v.getMarque() + " " + v.getModele() + " (" + v.getImmatriculation() + ")"));

        // • Rechercher une voiture avec findById()
        Vehicule retrievedVehicule = vehiculeService.findById(savedVehicule.getIdVehicule());
        assertNotNull(retrievedVehicule);
        assertEquals("Peugeot", retrievedVehicule.getMarque());
        assertEquals("3008", retrievedVehicule.getModele());
        System.out.println("Véhicule récupéré (findById) : " + retrievedVehicule.getMarque() + " " + retrievedVehicule.getModele() + " - Tarif : " + retrievedVehicule.getTarifJournalier());

        // • Modifier une propriété du véhicule puis utiliser update() pour enregistrer la modification
        retrievedVehicule.setTarifJournalier(new BigDecimal("110.00"));
        Vehicule updatedVehicule = vehiculeService.update(retrievedVehicule.getIdVehicule(), retrievedVehicule);
        assertEquals(new BigDecimal("110.00"), updatedVehicule.getTarifJournalier());
        System.out.println("Véhicule mis à jour (update) : nouveau tarif = " + updatedVehicule.getTarifJournalier());

        // • Afficher le nombre de véhicules avec count()
        System.out.println("Nombre de véhicules avec count() : " + vehiculeService.count());

        // • Supprimer le véhicule avec deleteById()
        vehiculeService.deleteById(savedVehicule.getIdVehicule());
        assertFalse(vehiculeService.existsById(savedVehicule.getIdVehicule()));
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.findById(savedVehicule.getIdVehicule()));
        System.out.println("Véhicule supprimé avec succès.");
    }

    // =========================================================================
    // Partie 5 – Exercice autonome (3 services choisis : Employe, Equipement, Maintenance)
    // =========================================================================

    @Test
    void testEmployeService() {
        System.out.println("=== TEST AUTONOME : EmployeService ===");
        // 1. Création avec create()
        Employe employe = new Employe();
        employe.setNom("Ben Salem");
        employe.setPrenom("Youssef");
        employe.setRole(RoleEmploye.AGENT);
        Employe savedEmploye = employeService.create(employe);
        assertNotNull(savedEmploye.getIdEmploye());
        System.out.println("Employé créé : " + savedEmploye.getNom() + " " + savedEmploye.getPrenom() + " (ID: " + savedEmploye.getIdEmploye() + ")");

        // 2. Lecture avec findAll() et findById()
        assertTrue(employeService.findAll().size() >= 1);
        Employe retrieved = employeService.findById(savedEmploye.getIdEmploye());
        assertNotNull(retrieved);
        assertEquals("Ben Salem", retrieved.getNom());
        System.out.println("Employé lu (findById) : " + retrieved.getNom() + " - Rôle : " + retrieved.getRole());

        // 3. Vérification avec existsById()
        assertTrue(employeService.existsById(savedEmploye.getIdEmploye()));

        // 4. Suppression avec deleteById() et vérification
        employeService.deleteById(savedEmploye.getIdEmploye());
        assertFalse(employeService.existsById(savedEmploye.getIdEmploye()));
        assertThrows(ResourceNotFoundException.class, () -> employeService.findById(savedEmploye.getIdEmploye()));
        System.out.println("Employé supprimé avec succès.");
    }

    @Test
    void testEquipementService() {
        System.out.println("=== TEST AUTONOME : EquipementService ===");
        // 1. Création avec create()
        Equipement equipement = new Equipement();
        equipement.setLibelle("Siège Enfant Isofix");
        Equipement savedEquipement = equipementService.create(equipement);
        assertNotNull(savedEquipement.getIdEquipement());
        System.out.println("Équipement créé : " + savedEquipement.getLibelle() + " (ID: " + savedEquipement.getIdEquipement() + ")");

        // 2. Lecture avec findAll() et findById()
        assertTrue(equipementService.findAll().size() >= 1);
        Equipement retrieved = equipementService.findById(savedEquipement.getIdEquipement());
        assertNotNull(retrieved);
        assertEquals("Siège Enfant Isofix", retrieved.getLibelle());
        System.out.println("Équipement lu (findById) : " + retrieved.getLibelle());

        // 3. Vérification avec existsById()
        assertTrue(equipementService.existsById(savedEquipement.getIdEquipement()));

        // 4. Suppression avec deleteById() et vérification
        equipementService.deleteById(savedEquipement.getIdEquipement());
        assertFalse(equipementService.existsById(savedEquipement.getIdEquipement()));
        assertThrows(ResourceNotFoundException.class, () -> equipementService.findById(savedEquipement.getIdEquipement()));
        System.out.println("Équipement supprimé avec succès.");
    }

    @Test
    void testMaintenanceService() {
        System.out.println("=== TEST AUTONOME : MaintenanceService ===");
        // 1. Création avec create()
        Maintenance maintenance = new Maintenance();
        maintenance.setDescription("Révision des 30 000 km et vidange");
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(2));
        Maintenance savedMaintenance = maintenanceService.create(maintenance);
        assertNotNull(savedMaintenance.getIdMaintenance());
        System.out.println("Maintenance créée : " + savedMaintenance.getDescription() + " (ID: " + savedMaintenance.getIdMaintenance() + ")");

        // 2. Lecture avec findAll() et findById()
        assertTrue(maintenanceService.findAll().size() >= 1);
        Maintenance retrieved = maintenanceService.findById(savedMaintenance.getIdMaintenance());
        assertNotNull(retrieved);
        assertEquals("Révision des 30 000 km et vidange", retrieved.getDescription());
        System.out.println("Maintenance lue (findById) : " + retrieved.getDescription() + " (Début: " + retrieved.getDateDebut() + ", Fin: " + retrieved.getDateFin() + ")");

        // 3. Vérification avec existsById()
        assertTrue(maintenanceService.existsById(savedMaintenance.getIdMaintenance()));

        // 4. Suppression avec deleteById() et vérification
        maintenanceService.deleteById(savedMaintenance.getIdMaintenance());
        assertFalse(maintenanceService.existsById(savedMaintenance.getIdMaintenance()));
        assertThrows(ResourceNotFoundException.class, () -> maintenanceService.findById(savedMaintenance.getIdMaintenance()));
        System.out.println("Maintenance supprimée avec succès.");
    }

    // =========================================================================
    // Tests de validation des règles métier
    // =========================================================================
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
