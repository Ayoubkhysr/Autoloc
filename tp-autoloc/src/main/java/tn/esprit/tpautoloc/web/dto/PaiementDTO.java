package tn.esprit.tpautoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.tpautoloc.domain.enums.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementDTO {
    private Long id;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être strictement positif")
    private BigDecimal montant;

    @NotNull(message = "La date de paiement est obligatoire")
    private LocalDate datePaiement;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;

    private Long contratId;
}
