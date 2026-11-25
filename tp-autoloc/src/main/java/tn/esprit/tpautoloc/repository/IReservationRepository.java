package tn.esprit.tpautoloc.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    // Atelier 8 - Exercice 3 : Recherche de réservations
    List<Reservation> findByClientIdClient(Long idClient);

    List<Reservation> findByVehiculeIdVehiculeAndStatutNot(Long vehiculeId, StatutReservation statut);

    List<Reservation> findByStatut(StatutReservation statut);

    List<Reservation> findByDateDebutBetween(LocalDate debut, LocalDate fin);

    List<Reservation> findByDateFinAfter(LocalDate date);

    List<Reservation> findByStatutAndVehiculeAgenceIdAgence(StatutReservation statut, Long agenceId);

    boolean existsByVehiculeIdVehiculeAndStatut(Long vehiculeId, StatutReservation statut);

    // Atelier 8 - Exercice 8 : Agrégations sur les réservations
    @Query("SELECT new tn.esprit.tpautoloc.web.dto.TopClientDTO(c.idClient, c.nom, c.prenom, COUNT(r)) " +
           "FROM Reservation r JOIN r.client c " +
           "GROUP BY c.idClient, c.nom, c.prenom " +
           "ORDER BY COUNT(r) DESC")
    List<TopClientDTO> topClients(Pageable pageable);
}
