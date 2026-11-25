package tn.esprit.tpautoloc.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratDTO {
    private Long id;
    private LocalDate dateSignature;

    @PositiveOrZero(message = "Le montant total doit être positif ou nul")
    private BigDecimal montantTotal;

    private Boolean valide;
    private Long reservationId;

    @Valid
    @Builder.Default
    private List<PaiementDTO> paiements = new ArrayList<>();
}
