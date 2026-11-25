package tn.esprit.tpautoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;

@Component
public class ReservationMapper {

    public ReservationResponseDTO toDto(Reservation reservation) {
        if (reservation == null) return null;

        Long clientId = (reservation.getClient() != null) ? reservation.getClient().getIdClient() : null;
        String clientNomComplet = (reservation.getClient() != null)
                ? (reservation.getClient().getPrenom() + " " + reservation.getClient().getNom())
                : null;
        Long vehiculeId = (reservation.getVehicule() != null) ? reservation.getVehicule().getIdVehicule() : null;
        String vehiculeImmatriculation = (reservation.getVehicule() != null) ? reservation.getVehicule().getImmatriculation() : null;

        return ReservationResponseDTO.builder()
                .id(reservation.getIdReservation())
                .dateDebut(reservation.getDateDebut())
                .dateFin(reservation.getDateFin())
                .statut(reservation.getStatut())
                .clientId(clientId)
                .clientNomComplet(clientNomComplet)
                .vehiculeId(vehiculeId)
                .vehiculeImmatriculation(vehiculeImmatriculation)
                .tarifEstime(null)
                .build();
    }

    public Reservation toEntity(ReservationRequestDTO dto) {
        if (dto == null) return null;
        Reservation reservation = new Reservation();
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        return reservation;
    }
}
