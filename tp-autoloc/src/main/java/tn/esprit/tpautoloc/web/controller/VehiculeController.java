package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/vehicules", "/jpql/vehicules"})
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion du parc automobile")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister tous les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> findAll() {
        return ResponseEntity.ok(vehiculeService.findAllDto());
    }

    @Operation(summary = "Consulter un véhicule par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findByIdDto(id));
    }

    @Operation(summary = "Créer un nouveau véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<VehiculeDTO> creer(@Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.creer(dto));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
    }

    // Atelier 7 : Affectation / Désaffectation
    @Operation(summary = "Affecter un véhicule à une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule affecté avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule ou agence introuvable")
    })
    @PutMapping("/{vehiculeId}/agence/{agenceId}")
    public ResponseEntity<Void> affecterVehiculeAAgence(@PathVariable Long vehiculeId, @PathVariable Long agenceId) {
        vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Désaffecter un véhicule de son agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule désaffecté avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @PutMapping("/{vehiculeId}/desaffecter")
    public ResponseEntity<Void> desaffecterVehiculeDeAgence(@PathVariable Long vehiculeId) {
        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
        return ResponseEntity.ok().build();
    }

    // Atelier 8 : Query Methods & JPQL
    @Operation(summary = "Recherche de véhicules par ville d'agence (Query Method)")
    @GetMapping({"/ville", "/recherche"})
    public ResponseEntity<List<VehiculeDTO>> findByAgenceVille(@RequestParam String ville) {
        return ResponseEntity.ok(vehiculeService.findByAgenceVille(ville));
    }

    @Operation(summary = "Recherche de véhicules par statut avec pagination (Query Method)")
    @GetMapping({"/statut", "/recherche/statut"})
    public ResponseEntity<Page<VehiculeDTO>> findByStatut(
            @RequestParam StatutVehicule statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(vehiculeService.findByStatut(statut, PageRequest.of(page, size)));
    }

    @Operation(summary = "Recherche personnalisée JPQL de véhicules disponibles par période")
    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculeDTO>> rechercherDisponibles(
            @RequestParam String ville,
            @RequestParam CategorieVehicule categorie,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(vehiculeService.rechercherVehiculesDisponibles(ville, categorie, dateDebut, dateFin));
    }
}
