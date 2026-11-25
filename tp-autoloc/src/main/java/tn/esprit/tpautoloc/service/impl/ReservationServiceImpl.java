package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.observer.ReservationSubject;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;
import tn.esprit.tpautoloc.web.mapper.ReservationMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationSubject reservationSubject;
    private final ReservationMapper reservationMapper;
    private final TarificationStrategyFactory tarificationStrategyFactory;

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
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable", id));
    }

    @Override
    @Transactional(readOnly = true)
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
            throw new ResourceNotFoundException("Reservation introuvable", id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return reservationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }

    @Override
    public Reservation creerReservation(Client client) {
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(3));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationRepository.save(reservation);
        reservationSubject.notifierConfirmation(saved);
        return saved;
    }

    @Override
    public Reservation annulerReservation(Reservation reservation) {
        Reservation existante = findById(reservation.getIdReservation());
        existante.setStatut(StatutReservation.ANNULEE);
        Reservation saved = reservationRepository.save(existante);
        reservationSubject.notifierAnnulation(saved);
        return saved;
    }

    // Atelier 7 : DTO
    @Override
    public ReservationResponseDTO creer(ReservationRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable", dto.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable", dto.getVehiculeId()));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationRepository.save(reservation);

        // Notifier les observateurs (Atelier 6)
        reservationSubject.notifierConfirmation(saved);

        ReservationResponseDTO responseDTO = reservationMapper.toDto(saved);
        if (tarificationStrategyFactory != null) {
            try {
                ITarificationStrategy strategy = tarificationStrategyFactory.resoudre(saved);
                if (strategy != null) {
                    responseDTO.setTarifEstime(strategy.calculerTarif(vehicule, saved));
                }
            } catch (Exception e) {
                log.warn("Impossible de calculer le tarif estimé : {}", e.getMessage());
            }
        }
        return responseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findByIdDto(Long id) {
        return reservationMapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAllDto() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    // Atelier 8 : JPQL
    @Override
    @Transactional(readOnly = true)
    public List<TopClientDTO> topClients(Pageable pageable) {
        return reservationRepository.topClients(pageable);
    }
}
