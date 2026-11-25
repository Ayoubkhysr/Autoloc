package tn.esprit.tpautoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

@Component
public class PaiementMapper {

    public PaiementDTO toDto(Paiement paiement) {
        if (paiement == null) return null;
        return PaiementDTO.builder()
                .id(paiement.getIdPaiement())
                .montant(paiement.getMontant())
                .datePaiement(paiement.getDatePaiement())
                .modePaiement(paiement.getModePaiement())
                .contratId(paiement.getContrat() != null ? paiement.getContrat().getIdContrat() : null)
                .build();
    }

    public Paiement toEntity(PaiementDTO dto) {
        if (dto == null) return null;
        Paiement paiement = new Paiement();
        paiement.setIdPaiement(dto.getId());
        paiement.setMontant(dto.getMontant());
        paiement.setDatePaiement(dto.getDatePaiement());
        paiement.setModePaiement(dto.getModePaiement());
        return paiement;
    }
}
