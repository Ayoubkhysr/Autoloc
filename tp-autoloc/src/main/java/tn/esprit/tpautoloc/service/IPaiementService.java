package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

public interface IPaiementService {
    Paiement findById(Long id);
    List<Paiement> findAll();

    // Atelier 7 : DTO
    PaiementDTO findByIdDto(Long id);
    List<PaiementDTO> findAllDto();
}
