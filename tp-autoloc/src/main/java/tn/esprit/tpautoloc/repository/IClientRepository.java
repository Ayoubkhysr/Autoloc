package tn.esprit.tpautoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Client;

import java.util.List;
import java.util.Optional;

public interface IClientRepository extends JpaRepository<Client, Long> {

    // Atelier 8 - Exercice 2 : Recherche de clients
    Optional<Client> findByEmail(String email);

    Optional<Client> findByNumPermis(String numPermis);

    List<Client> findByNomContaining(String nom);

    boolean existsByEmail(String email);
}
