package tn.esprit.tpautoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

@Component
public class VehiculeMapper {

    public VehiculeDTO toDto(Vehicule vehicule) {
        if (vehicule == null) return null;
        return VehiculeDTO.builder()
                .id(vehicule.getIdVehicule())
                .immatriculation(vehicule.getImmatriculation())
                .marque(vehicule.getMarque())
                .modele(vehicule.getModele())
                .categorie(vehicule.getCategorie())
                .tarifJournalier(vehicule.getTarifJournalier())
                .statut(vehicule.getStatut())
                .agenceId(vehicule.getAgence() != null ? vehicule.getAgence().getIdAgence() : null)
                .build();
    }

    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null) return null;
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(dto.getId());
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setCategorie(dto.getCategorie());
        vehicule.setTarifJournalier(dto.getTarifJournalier());
        vehicule.setStatut(dto.getStatut());
        return vehicule;
    }
}
