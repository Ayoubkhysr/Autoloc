package tn.esprit.tpautoloc.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;
import tn.esprit.tpautoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public Vehicule create(Vehicule vehicule) {
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        if (vehicule.getTarifJournalier() != null && vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        if (vehicule.getTarifJournalier() != null && vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
        Vehicule existant = findById(id);
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        existant.setAgence(vehicule.getAgence());
        existant.setEquipements(vehicule.getEquipements());
        return vehiculeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Véhicule introuvable", id);
        }
        vehiculeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return vehiculeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return vehiculeRepository.existsById(id);
    }

    // Atelier 7 : Affectation / Désaffectation
    @Override
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable", vehiculeId));
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", agenceId));
        vehicule.setAgence(agence);
        vehiculeRepository.save(vehicule);
    }

    @Override
    public void desaffecterVehiculeDeAgence(Long vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable", vehiculeId));
        vehicule.setAgence(null);
        vehiculeRepository.save(vehicule);
    }

    // Atelier 7 : DTO
    @Override
    public VehiculeDTO creer(VehiculeDTO dto) {
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", dto.getAgenceId()));
            vehicule.setAgence(agence);
        }
        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO findByIdDto(Long id) {
        return vehiculeMapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findAllDto() {
        return vehiculeRepository.findAll().stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    // Atelier 8 : Query Methods & JPQL
    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findByAgenceVille(String ville) {
        return vehiculeRepository.findByAgenceVille(ville).stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findByStatut(StatutVehicule statut, Pageable pageable) {
        return vehiculeRepository.findByStatut(statut, pageable)
                .map(vehiculeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> rechercherVehiculesDisponibles(String ville, CategorieVehicule categorie, LocalDate dateDebut, LocalDate dateFin) {
        return vehiculeRepository.rechercherVehiculesDisponibles(ville, categorie, dateDebut, dateFin).stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }
}
