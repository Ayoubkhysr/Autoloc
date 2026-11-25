package tn.esprit.tpautoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.web.dto.ContratDTO;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContratMapper {

    private final PaiementMapper paiementMapper;

    public ContratDTO toDto(Contrat contrat) {
        if (contrat == null) return null;
        List<tn.esprit.tpautoloc.web.dto.PaiementDTO> paiementsDto = new ArrayList<>();
        if (contrat.getPaiements() != null) {
            paiementsDto = contrat.getPaiements().stream()
                    .map(paiementMapper::toDto)
                    .toList();
        }
        return ContratDTO.builder()
                .id(contrat.getIdContrat())
                .dateSignature(contrat.getDateSignature())
                .montantTotal(contrat.getMontantTotal())
                .valide(contrat.isValide())
                .reservationId(contrat.getReservation() != null ? contrat.getReservation().getIdReservation() : null)
                .paiements(paiementsDto)
                .build();
    }

    public Contrat toEntity(ContratDTO dto) {
        if (dto == null) return null;
        Contrat contrat = new Contrat();
        contrat.setIdContrat(dto.getId());
        contrat.setDateSignature(dto.getDateSignature());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.getValide());

        if (dto.getPaiements() != null) {
            List<Paiement> paiements = new ArrayList<>();
            for (var pDto : dto.getPaiements()) {
                Paiement p = paiementMapper.toEntity(pDto);
                p.setContrat(contrat);
                paiements.add(p);
            }
            contrat.setPaiements(paiements);
        }
        return contrat;
    }
}
