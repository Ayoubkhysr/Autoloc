package tn.esprit.tpautoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

@Component
public class EmployeMapper {

    public EmployeDTO toDto(Employe employe) {
        if (employe == null) return null;
        return EmployeDTO.builder()
                .id(employe.getIdEmploye())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .role(employe.getRole())
                .agenceId(employe.getAgence() != null ? employe.getAgence().getIdAgence() : null)
                .build();
    }

    public Employe toEntity(EmployeDTO dto) {
        if (dto == null) return null;
        Employe employe = new Employe();
        employe.setIdEmploye(dto.getId());
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setRole(dto.getRole());
        return employe;
    }
}
