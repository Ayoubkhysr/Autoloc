package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO;
import tn.esprit.tpautoloc.web.dto.ContratDTO;

public interface IContratService {
    Contrat create(Contrat contrat);
    Contrat findById(Long id);
    List<Contrat> findAll();
    Contrat update(Long id, Contrat contrat);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);

    // Atelier 7 : DTO
    ContratDTO creer(ContratDTO dto);
    ContratDTO findByIdDto(Long id);
    List<ContratDTO> findAllDto();

    // Atelier 8 : JPQL
    List<ChiffreAffaireAgenceDTO> chiffreAffaireParAgence();
}
