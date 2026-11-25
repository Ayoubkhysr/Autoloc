package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IPaiementRepository;
import tn.esprit.tpautoloc.service.IPaiementService;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;
import tn.esprit.tpautoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable", id));
    }

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    public PaiementDTO findByIdDto(Long id) {
        return paiementMapper.toDto(findById(id));
    }

    @Override
    public List<PaiementDTO> findAllDto() {
        return paiementRepository.findAll().stream()
                .map(paiementMapper::toDto)
                .toList();
    }
}
