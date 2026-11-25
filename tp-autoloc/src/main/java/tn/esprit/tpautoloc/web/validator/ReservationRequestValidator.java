package tn.esprit.tpautoloc.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;

import java.time.LocalDate;

@Component
public class ReservationRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> classe) {
        return ReservationRequestDTO.class.equals(classe);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservationRequestDTO dto = (ReservationRequestDTO) target;
        if (dto.getDateDebut() == null || dto.getDateFin() == null) {
            return; // Les champs obligatoires sont traités par Bean Validation.
        }
        if (dto.getDateDebut().isBefore(LocalDate.now())) {
            errors.rejectValue("dateDebut",
                    "reservation.dateDebut.passee",
                    "La date de début doit être aujourd'hui ou dans le futur");
        }
        if (!dto.getDateFin().isAfter(dto.getDateDebut())) {
            errors.rejectValue("dateFin",
                    "reservation.dates.invalides",
                    "La date de fin doit être postérieure à la date de début");
        }
    }
}
