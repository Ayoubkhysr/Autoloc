package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employés", description = "Gestion du personnel de l'agence")
public class EmployeController {

    private final IEmployeService employeService;

    @Operation(summary = "Lister tous les employés")
    @ApiResponse(responseCode = "200", description = "Liste des employés récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<EmployeDTO>> findAll() {
        return ResponseEntity.ok(employeService.findAllDto());
    }

    @Operation(summary = "Consulter un employé par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> findById(@Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        return ResponseEntity.ok(employeService.findByIdDto(id));
    }

    @Operation(summary = "Créer un nouvel employé")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employé créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<EmployeDTO> creer(@Valid @RequestBody EmployeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeService.creer(dto));
    }

    @Operation(summary = "Supprimer un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employé supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        employeService.deleteById(id);
    }

    // Atelier 7 : Affectation / Désaffectation
    @Operation(summary = "Affecter un employé à une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé affecté avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé ou agence introuvable")
    })
    @PutMapping("/{employeId}/agence/{agenceId}")
    public ResponseEntity<Void> affecterEmployeAAgence(@PathVariable Long employeId, @PathVariable Long agenceId) {
        employeService.affecterEmployeAAgence(employeId, agenceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé désaffecté avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @PutMapping("/{employeId}/desaffecter")
    public ResponseEntity<Void> desaffecterEmployeDeAgence(@PathVariable Long employeId) {
        employeService.desaffecterEmployeDeAgence(employeId);
        return ResponseEntity.ok().build();
    }
}
