package tn.esprit.tpautoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO;

import java.time.LocalDate;
import java.util.List;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    // Atelier 8 - Exercice 9 : Agrégations et DTO sur les contrats
    @Query("SELECT new tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO(" +
           "a.idAgence, a.nom, SUM(c.montantTotal)) " +
           "FROM Contrat c " +
           "JOIN c.reservation r " +
           "JOIN r.vehicule v " +
           "JOIN v.agence a " +
           "WHERE c.valide = true " +
           "GROUP BY a.idAgence, a.nom")
    List<ChiffreAffaireAgenceDTO> chiffreAffaireParAgence();

    @Query("SELECT c FROM Contrat c JOIN c.reservation r " +
           "WHERE r.dateFin BETWEEN :aujourdhui AND :dateLimite")
    List<Contrat> trouverContratsAEcheanceProche(
            @Param("aujourdhui") LocalDate aujourdhui,
            @Param("dateLimite") LocalDate dateLimite);
}
