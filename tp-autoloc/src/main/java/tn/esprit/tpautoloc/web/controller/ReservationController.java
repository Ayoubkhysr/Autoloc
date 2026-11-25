package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IContratService;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Réservations", description = "Gestion des réservations de véhicules")
public class ReservationController {

    private final IReservationService reservationService;
    private final IContratService contratService;
    private final ReservationRequestValidator reservationRequestValidator;

    @Operation(summary = "Lister toutes les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> findAll() {
        return ResponseEntity.ok(reservationService.findAllDto());
    }

    @Operation(summary = "Consulter une réservation par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> findById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findByIdDto(id));
    }

    @Operation(summary = "Créer une réservation avec Bean Validation et Validateur personnalisé")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données ou dates invalides")
    })
    @PostMapping
    public ResponseEntity<?> creer(
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {

        reservationRequestValidator.validate(dto, result);
        if (result.hasErrors()) {
            Map<String, String> erreurs = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    erreurs.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(erreurs);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.creer(dto));
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        reservationService.deleteById(id);
    }

    // Atelier 8 : JPQL Top clients
    @Operation(summary = "Top des clients par nombre de réservations (JPQL)")
    @GetMapping("/top-clients")
    public ResponseEntity<List<TopClientDTO>> topClients(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(reservationService.topClients(PageRequest.of(0, limit)));
    }

    // Atelier 8 : JPQL Chiffre d'affaires par agence
    @Operation(summary = "Chiffre d'affaires par agence (JPQL)")
    @GetMapping({"/chiffre-affaires-agences", "/chiffre-affaires/agences"})
    public ResponseEntity<List<ChiffreAffaireAgenceDTO>> chiffreAffaireParAgence() {
        return ResponseEntity.ok(contratService.chiffreAffaireParAgence());
    }
}
