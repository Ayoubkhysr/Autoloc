package tn.esprit.tpautoloc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {

    // Atelier 8 - Exercice 1 : Query Methods
    List<Vehicule> findByAgenceVille(String ville);

    List<Vehicule> findByStatutAndAgenceIdAgence(StatutVehicule statut, Long agenceId);

    Page<Vehicule> findByStatut(StatutVehicule statut, Pageable pageable);

    List<Vehicule> findByAgenceVilleAndCategorie(String ville, CategorieVehicule categorie);

    boolean existsByIdVehiculeAndStatut(Long vehiculeId, StatutVehicule statut);

    long countByStatut(StatutVehicule statut);

    List<Vehicule> findByTarifJournalierLessThan(BigDecimal tarif);

    List<Vehicule> findByTarifJournalierGreaterThan(BigDecimal tarif);

    List<Vehicule> findAllByOrderByTarifJournalierAsc();

    // Atelier 8 - Exercice 7 : Recherches personnalisées JPQL
    @Query("SELECT v FROM Vehicule v WHERE v.agence.ville = :ville " +
           "AND v.categorie = :categorie " +
           "AND v.statut = tn.esprit.tpautoloc.domain.enums.StatutVehicule.DISPONIBLE " +
           "AND v.idVehicule NOT IN (" +
           "   SELECT r.vehicule.idVehicule FROM Reservation r " +
           "   WHERE r.vehicule IS NOT NULL " +
           "   AND r.statut <> tn.esprit.tpautoloc.domain.enums.StatutReservation.ANNULEE " +
           "   AND r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut" +
           ")")
    List<Vehicule> rechercherVehiculesDisponibles(
            @Param("ville") String ville,
            @Param("categorie") CategorieVehicule categorie,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    @Query("SELECT DISTINCT v FROM Vehicule v JOIN v.reservations r " +
           "WHERE r.dateFin = :aujourdhui " +
           "AND r.statut = tn.esprit.tpautoloc.domain.enums.StatutReservation.CONFIRMEE")
    List<Vehicule> trouverVehiculesAReleaser(@Param("aujourdhui") LocalDate aujourdhui);
}
