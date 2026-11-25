package tn.esprit.tpautoloc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

import java.time.LocalDate;
import java.util.List;

public interface IVehiculeService {
    Vehicule create(Vehicule vehicule);
    Vehicule findById(Long id);
    List<Vehicule> findAll();
    Vehicule update(Long id, Vehicule vehicule);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);

    // Atelier 7 : Affectation / Désaffectation
    void affecterVehiculeAAgence(Long vehiculeId, Long agenceId);
    void desaffecterVehiculeDeAgence(Long vehiculeId);

    // Atelier 7 : Méthodes DTO
    VehiculeDTO creer(VehiculeDTO dto);
    VehiculeDTO findByIdDto(Long id);
    List<VehiculeDTO> findAllDto();

    // Atelier 8 : Query Methods & JPQL
    List<VehiculeDTO> findByAgenceVille(String ville);
    Page<VehiculeDTO> findByStatut(StatutVehicule statut, Pageable pageable);
    List<VehiculeDTO> rechercherVehiculesDisponibles(String ville, CategorieVehicule categorie, LocalDate dateDebut, LocalDate dateFin);
}
