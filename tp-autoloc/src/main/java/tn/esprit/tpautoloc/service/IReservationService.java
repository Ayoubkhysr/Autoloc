package tn.esprit.tpautoloc.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;

public interface IReservationService {
    Reservation create(Reservation reservation);
    Reservation findById(Long id);
    List<Reservation> findAll();
    Reservation update(Long id, Reservation reservation);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
    Reservation creerReservation(Client client);
    Reservation annulerReservation(Reservation reservation);

    // Atelier 7 : DTO
    ReservationResponseDTO creer(ReservationRequestDTO dto);
    ReservationResponseDTO findByIdDto(Long id);
    List<ReservationResponseDTO> findAllDto();

    // Atelier 8 : JPQL Top clients
    List<TopClientDTO> topClients(Pageable pageable);
}
