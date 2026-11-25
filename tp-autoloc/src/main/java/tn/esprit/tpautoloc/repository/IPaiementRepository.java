package tn.esprit.tpautoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Paiement;

import java.time.LocalDate;
import java.util.List;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {

    // Atelier 8 - Exercice 6 : Recherche de paiements
    List<Paiement> findByContratIdContrat(Long contratId);

    List<Paiement> findByDatePaiementBetween(LocalDate debut, LocalDate fin);
}
