package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.domain.Maintenance;
import tn.esprit.tpautoloc.service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion des entretiens et réparations des véhicules")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @Operation(summary = "Lister toutes les opérations de maintenance")
    @ApiResponse(responseCode = "200", description = "Liste des maintenances récupérée avec succès")
    @GetMapping
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @Operation(summary = "Consulter une maintenance par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @GetMapping("/{id}")
    public Maintenance findById(@Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        return maintenanceService.findById(id);
    }

    @Operation(summary = "Planifier une nouvelle maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Maintenance enregistrée avec succès"),
            @ApiResponse(responseCode = "400", description = "Date de fin antérieure à la date de début ou données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Maintenance create(@RequestBody Maintenance maintenance) {
        return maintenanceService.create(maintenance);
    }

    @Operation(summary = "Modifier une maintenance existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @PutMapping("/{id}")
    public Maintenance update(@Parameter(description = "Identifiant de la maintenance à modifier") @PathVariable Long id,
                             @RequestBody Maintenance maintenance) {
        return maintenanceService.update(id, maintenance);
    }

    @Operation(summary = "Supprimer une maintenance par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Maintenance supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de la maintenance à supprimer") @PathVariable Long id) {
        maintenanceService.deleteById(id);
    }
}
