package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;
import tn.esprit.tpautoloc.web.mapper.EmployeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public Employe create(Employe employe) {
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouvel employé ne doit pas avoir d'identifiant");
        }
        if (employe.getNom() == null || employe.getNom().trim().isEmpty() || employe.getRole() == null) {
            throw new IllegalArgumentException("Le nom et le rôle de l'employé sont obligatoires");
        }
        return employeRepository.save(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        if (employe.getNom() == null || employe.getNom().trim().isEmpty() || employe.getRole() == null) {
            throw new IllegalArgumentException("Le nom et le rôle de l'employé sont obligatoires");
        }
        Employe existant = findById(id);
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        existant.setAgence(employe.getAgence());
        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employé introuvable", id);
        }
        employeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return employeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return employeRepository.existsById(id);
    }

    // Atelier 7 : Affectation / Désaffectation
    @Override
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable", employeId));
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", agenceId));
        employe.setAgence(agence);
        employeRepository.save(employe);
    }

    @Override
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable", employeId));
        employe.setAgence(null);
        employeRepository.save(employe);
    }

    // Atelier 7 : DTO
    @Override
    public EmployeDTO creer(EmployeDTO dto) {
        Employe employe = employeMapper.toEntity(dto);
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", dto.getAgenceId()));
            employe.setAgence(agence);
        }
        Employe saved = employeRepository.save(employe);
        return employeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO findByIdDto(Long id) {
        return employeMapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> findAllDto() {
        return employeRepository.findAll().stream()
                .map(employeMapper::toDto)
                .toList();
    }
}
