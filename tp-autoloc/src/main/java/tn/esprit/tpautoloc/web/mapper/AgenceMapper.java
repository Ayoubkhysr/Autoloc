package tn.esprit.tpautoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;

@Component
public class AgenceMapper {

    public AgenceDTO toDto(Agence agence) {
        if (agence == null) return null;
        return AgenceDTO.builder()
                .id(agence.getIdAgence())
                .nom(agence.getNom())
                .ville(agence.getVille())
                .adresse(agence.getAdresse())
                .telephone(agence.getTelephone())
                .build();
    }

    public Agence toEntity(AgenceDTO dto) {
        if (dto == null) return null;
        Agence agence = new Agence();
        agence.setIdAgence(dto.getId());
        agence.setNom(dto.getNom());
        agence.setVille(dto.getVille());
        agence.setAdresse(dto.getAdresse());
        agence.setTelephone(dto.getTelephone());
        return agence;
    }
}
