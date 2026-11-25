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
import tn.esprit.tpautoloc.service.IContratService;
import tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO;
import tn.esprit.tpautoloc.web.dto.ContratDTO;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Lister tous les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<ContratDTO>> findAll() {
        return ResponseEntity.ok(contratService.findAllDto());
    }

    @Operation(summary = "Consulter un contrat par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContratDTO> findById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return ResponseEntity.ok(contratService.findByIdDto(id));
    }

    @Operation(summary = "Créer un nouveau contrat avec ses paiements en cascade")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<ContratDTO> creer(@Valid @RequestBody ContratDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.creer(dto));
    }

    @Operation(summary = "Supprimer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteById(id);
    }

    // Atelier 8 : JPQL Chiffre d'affaires
    @Operation(summary = "Calcul du chiffre d'affaires par agence (JPQL)")
    @GetMapping("/chiffre-affaires-agences")
    public ResponseEntity<List<ChiffreAffaireAgenceDTO>> chiffreAffaireParAgence() {
        return ResponseEntity.ok(contratService.chiffreAffaireParAgence());
    }
}
