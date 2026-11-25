package tn.esprit.tpautoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;

import java.util.List;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {

    // Atelier 8 - Exercice 4 : Recherche d'employés
    List<Employe> findByAgenceNom(String nomAgence);

    List<Employe> findByAgenceIdAgenceAndRole(Long agenceId, RoleEmploye role);

    List<Employe> findByNomContaining(String nom);
}
