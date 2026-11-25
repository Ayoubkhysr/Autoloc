package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

public interface IEmployeService {
    Employe create(Employe employe);
    Employe findById(Long id);
    List<Employe> findAll();
    Employe update(Long id, Employe employe);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);

    // Atelier 7 : Affectation / Désaffectation
    void affecterEmployeAAgence(Long employeId, Long agenceId);
    void desaffecterEmployeDeAgence(Long employeId);

    // Atelier 7 : DTO
    EmployeDTO creer(EmployeDTO dto);
    EmployeDTO findByIdDto(Long id);
    List<EmployeDTO> findAllDto();
}
