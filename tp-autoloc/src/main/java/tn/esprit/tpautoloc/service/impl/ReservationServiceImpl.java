package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.observer.ReservationSubject;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.service.IReservationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final ReservationSubject reservationSubject;

    @Override
    public Reservation create(Reservation reservation) {
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null && reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null && reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
        Reservation existant = findById(id);
        existant.setDateDebut(reservation.getDateDebut());
        existant.setDateFin(reservation.getDateFin());
        existant.setStatut(reservation.getStatut());
        existant.setClient(reservation.getClient());
        existant.setVehicule(reservation.getVehicule());
        existant.setContrat(reservation.getContrat());
        return reservationRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public long count() {
        return reservationRepository.count();
    }

    @Override
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }

    /**
     * Crée une réservation et notifie tous les observateurs (Pattern Observer).
     */
    @Override
    public Reservation creerReservation(Client client) {
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(3));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationRepository.save(reservation);

        // Déclenche le process métier via le pattern Observer
        reservationSubject.notifierConfirmation(saved);
        return saved;
    }

    /**
     * Annule une réservation existante et notifie tous les observateurs (Pattern Observer).
     */
    @Override
    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE);
        String clientEmail = (reservation.getClient() != null) ? reservation.getClient().getEmail() : "inconnu";
        log.info("Reservation #{} annulée pour le client {}", reservation.getIdReservation(), clientEmail);

        Reservation updated = reservationRepository.save(reservation);

        // Déclenche le process métier via le pattern Observer
        reservationSubject.notifierAnnulation(updated);
        return updated;
    }
}
