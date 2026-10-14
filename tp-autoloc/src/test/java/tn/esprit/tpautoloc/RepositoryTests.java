package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryTests {

    @Autowired
    private IAgenceRepository agenceRepository;
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private IVehiculeRepository vehiculeRepository;
    
    @Autowired
    private IEmployeRepository employeRepository;
    @Autowired
    private IEquipementRepository equipementRepository;
    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    // Partie 2 – Test guidé de AgenceRepository
    @Test
    void testAgenceRepository() {
        Agence agence = new Agence();
        agence.setNom("AutoLoc Paris");
        agence.setVille("Paris");
        agence.setAdresse("10 Rue de la Paix");
        agence.setTelephone("0123456789");

        Agence savedAgence = agenceRepository.save(agence);

        System.out.println("Agence enregistrée : " + savedAgence.getNom() + " - ID: " + savedAgence.getIdAgence());
        assertNotNull(savedAgence.getIdAgence(), "L'ID ne doit pas être null après l'enregistrement");

        System.out.println("Nombre total d'agences : " + agenceRepository.findAll().size());

        Agence retrievedAgence = agenceRepository.findById(savedAgence.getIdAgence()).orElse(null);
        assertNotNull(retrievedAgence);
        System.out.println("Agence récupérée : " + retrievedAgence.getNom());

        assertTrue(agenceRepository.existsById(savedAgence.getIdAgence()));

        System.out.println("Nombre d'agences dans la base : " + agenceRepository.count());

        agenceRepository.deleteById(savedAgence.getIdAgence());
        assertFalse(agenceRepository.existsById(savedAgence.getIdAgence()));
        System.out.println("Agence supprimée avec succès.");
    }

    // Partie 3 – Test guidé de ClientRepository
    @Test
    void testClientRepository() {
        Client client1 = new Client();
        client1.setNom("khalfallah");
        client1.setPrenom("ayoub");
        client1.setEmail("ayoub@gmail.com");
        
        Client client2 = new Client();
        client2.setNom("ali");
        client2.setPrenom("ouni");
        client2.setEmail("ali@test.com");

        client1 = clientRepository.save(client1);
        client2 = clientRepository.save(client2);
        assertNotNull(client1.getIdClient());
        assertNotNull(client2.getIdClient());

        System.out.println("Liste des clients, taille : " + clientRepository.findAll().size());

        Client retrievedClient = clientRepository.findById(client1.getIdClient()).orElse(null);
        assertNotNull(retrievedClient);
        assertEquals("khalfallah", retrievedClient.getNom());

        assertTrue(clientRepository.existsById(client1.getIdClient()));
        assertFalse(clientRepository.existsById(999999L)); // Identifiant supposé inexistant

        System.out.println("Nombre total de clients : " + clientRepository.count());
        
        clientRepository.deleteById(client1.getIdClient());
        clientRepository.deleteById(client2.getIdClient());
    }

    // Partie 4 – Test guidé de VehiculeRepository
    @Test
    void testVehiculeRepository() {
        // Créer un véhicule avec les attributs de l'entité
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("AB-123-CD");
        vehicule.setMarque("Peugeot");
        vehicule.setModele("208");
        vehicule.setTarifJournalier(new BigDecimal("45.00"));

        // L’enregistrer avec save()
        vehicule = vehiculeRepository.save(vehicule);
        assertNotNull(vehicule.getIdVehicule());

        // Afficher tous les véhicules avec findAll()
        System.out.println("Nombre de véhicules : " + vehiculeRepository.findAll().size());

        // Afficher le nombre de véhicules avec count()
        System.out.println("Count véhicules : " + vehiculeRepository.count());

        // Modifier une propriété du véhicule puis utiliser save() pour enregistrer la modification
        vehicule.setTarifJournalier(new BigDecimal("50.00"));
        Vehicule vehiculeModifie = vehiculeRepository.save(vehicule);
        assertEquals(new BigDecimal("50.00"), vehiculeModifie.getTarifJournalier());

        // Supprimer le véhicule avec deleteById()
        vehiculeRepository.deleteById(vehicule.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(vehicule.getIdVehicule()));
    }

    // Partie 5 – Exercice autonome (3 repositories au choix)
    
    @Test
    void testEmployeRepository() {
        Employe employe = new Employe();
        employe.setNom("Durand");
        employe.setPrenom("Paul");
        
        employe = employeRepository.save(employe);
        assertNotNull(employe.getIdEmploye());
        
        assertTrue(employeRepository.existsById(employe.getIdEmploye()));
        System.out.println("Employés trouvés : " + employeRepository.findAll().size());
        
        employeRepository.deleteById(employe.getIdEmploye());
        assertFalse(employeRepository.existsById(employe.getIdEmploye()));
    }

    @Test
    void testEquipementRepository() {
        Equipement equipement = new Equipement();
        equipement.setLibelle("Siège bébé");
        
        equipement = equipementRepository.save(equipement);
        assertNotNull(equipement.getIdEquipement());
        
        assertNotNull(equipementRepository.findById(equipement.getIdEquipement()).orElse(null));
        
        equipementRepository.deleteById(equipement.getIdEquipement());
        assertFalse(equipementRepository.existsById(equipement.getIdEquipement()));
    }

    @Test
    void testMaintenanceRepository() {
        Maintenance maintenance = new Maintenance();
        maintenance.setDescription("Changement des pneus");
        maintenance.setDateDebut(LocalDate.now());
        
        maintenance = maintenanceRepository.save(maintenance);
        assertNotNull(maintenance.getIdMaintenance());
        
        assertTrue(maintenanceRepository.existsById(maintenance.getIdMaintenance()));
        
        maintenanceRepository.deleteById(maintenance.getIdMaintenance());
        assertFalse(maintenanceRepository.existsById(maintenance.getIdMaintenance()));
    }
}
