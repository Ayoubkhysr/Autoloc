package tn.esprit.tpautoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeDTO {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String prenom;
    private RoleEmploye role;
    private Long agenceId;
}
