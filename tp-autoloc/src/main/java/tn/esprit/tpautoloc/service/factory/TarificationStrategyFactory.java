package tn.esprit.tpautoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Sélectionne dynamiquement la TarificationStrategy adaptée à une réservation
 * (durée du séjour, week-end ou standard). Toutes les implémentations Spring de
 * ITarificationStrategy sont injectées automatiquement sous forme de liste.
 */
@Component
public class TarificationStrategyFactory {

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(List<ITarificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ITarificationStrategy::getCode, Function.identity()));
    }

    public ITarificationStrategy resoudre(Reservation reservation) {
        if (reservation == null || reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            return strategies.get("STANDARD");
        }
        long duree = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        if (duree >= 7) {
            return strategies.get("LONGUE_DUREE");
        } else if (estWeekend(reservation)) {
            return strategies.get("WEEKEND");
        } else {
            return strategies.get("STANDARD");
        }
    }

    private boolean estWeekend(Reservation reservation) {
        if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            return false;
        }
        DayOfWeek debut = reservation.getDateDebut().getDayOfWeek();
        DayOfWeek fin = reservation.getDateFin().getDayOfWeek();
        long duree = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        return duree <= 3 && (debut == DayOfWeek.FRIDAY || debut == DayOfWeek.SATURDAY || fin == DayOfWeek.SUNDAY);
    }
}
