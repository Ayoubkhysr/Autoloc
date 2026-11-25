package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;

public interface IAgenceService {
    Agence create(Agence agence);
    Agence findById(Long id);
    List<Agence> findAll();
    Agence update(Long id, Agence agence);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);

    // Atelier 7 : DTO
    AgenceDTO creer(AgenceDTO dto);
    AgenceDTO findByIdDto(Long id);
    List<AgenceDTO> findAllDto();
}
