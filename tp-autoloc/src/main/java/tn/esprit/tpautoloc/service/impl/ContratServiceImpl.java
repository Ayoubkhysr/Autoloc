package tn.esprit.tpautoloc.service.impl;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IContratRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.service.IContratService;
import tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO;
import tn.esprit.tpautoloc.web.dto.ContratDTO;
import tn.esprit.tpautoloc.web.mapper.ContratMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public Contrat create(Contrat contrat) {
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        if (contrat.getMontantTotal() != null && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
        if (contrat.getPaiements() != null) {
            for (var p : contrat.getPaiements()) {
                p.setContrat(contrat);
                if (p.getMontant() != null && p.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Le montant du paiement doit être strictement positif");
                }
            }
        }
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        if (contrat.getMontantTotal() != null && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
        Contrat existant = findById(id);
        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.isValide());
        return contratRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat introuvable", id);
        }
        contratRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return contratRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return contratRepository.existsById(id);
    }

    // Atelier 7 : DTO
    @Override
    public ContratDTO creer(ContratDTO dto) {
        Contrat contrat = contratMapper.toEntity(dto);
        if (dto.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(dto.getReservationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable", dto.getReservationId()));
            contrat.setReservation(reservation);
        }
        Contrat saved = create(contrat);
        return contratMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findByIdDto(Long id) {
        return contratMapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAllDto() {
        return contratRepository.findAll().stream()
                .map(contratMapper::toDto)
                .toList();
    }

    // Atelier 8 : JPQL
    @Override
    @Transactional(readOnly = true)
    public List<ChiffreAffaireAgenceDTO> chiffreAffaireParAgence() {
        return contratRepository.chiffreAffaireParAgence();
    }
}
