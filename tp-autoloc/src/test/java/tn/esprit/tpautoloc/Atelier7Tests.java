package tn.esprit.tpautoloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.mapper.AgenceMapper;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires validant les composants de l'Atelier 7 (Validateur personnalisé et Mappers).
 */
class Atelier7Tests {

    private ReservationRequestValidator validator;
    private AgenceMapper agenceMapper;

    @BeforeEach
    void setUp() {
        validator = new ReservationRequestValidator();
        agenceMapper = new AgenceMapper();
    }

    @Test
    @DisplayName("Le validateur doit accepter des dates cohérentes")
    void testDatesValides() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(LocalDate.now().plusDays(1))
                .dateFin(LocalDate.now().plusDays(5))
                .build();

        Errors errors = new BeanPropertyBindingResult(dto, "reservationRequestDTO");
        validator.validate(dto, errors);

        assertFalse(errors.hasErrors(), "Aucune erreur ne doit être remontée pour des dates valides");
    }

    @Test
    @DisplayName("Le validateur doit rejeter une date de fin antérieure ou égale à la date de début")
    void testDateFinInferieureOuEgaleDateDebut() {
        // Cas 1 : dateFin < dateDebut
        ReservationRequestDTO dtoInferieure = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(LocalDate.now().plusDays(10))
                .dateFin(LocalDate.now().plusDays(5))
                .build();

        Errors errors1 = new BeanPropertyBindingResult(dtoInferieure, "reservationRequestDTO");
        validator.validate(dtoInferieure, errors1);
        assertTrue(errors1.hasFieldErrors("dateFin"));
        assertThat(errors1.getFieldError("dateFin").getDefaultMessage()).contains("postérieure");

        // Cas 2 : dateFin == dateDebut
        ReservationRequestDTO dtoEgale = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(LocalDate.now().plusDays(10))
                .dateFin(LocalDate.now().plusDays(10))
                .build();

        Errors errors2 = new BeanPropertyBindingResult(dtoEgale, "reservationRequestDTO");
        validator.validate(dtoEgale, errors2);
        assertTrue(errors2.hasFieldErrors("dateFin"));
    }

    @Test
    @DisplayName("Le validateur doit rejeter une date de début dans le passé")
    void testDateDebutDansLePasse() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(LocalDate.now().minusDays(2))
                .dateFin(LocalDate.now().plusDays(5))
                .build();

        Errors errors = new BeanPropertyBindingResult(dto, "reservationRequestDTO");
        validator.validate(dto, errors);

        assertTrue(errors.hasFieldErrors("dateDebut"));
    }

    @Test
    @DisplayName("Le mapper AgenceMapper doit convertir correctement entre Entité et DTO")
    void testAgenceMapper() {
        Agence agence = new Agence();
        agence.setIdAgence(10L);
        agence.setNom("Agence Centrale");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        AgenceDTO dto = agenceMapper.toDto(agence);
        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Agence Centrale", dto.getNom());
        assertEquals("Tunis", dto.getVille());

        Agence converted = agenceMapper.toEntity(dto);
        assertNotNull(converted);
        assertEquals(10L, converted.getIdAgence());
        assertEquals("Agence Centrale", converted.getNom());
    }
}
