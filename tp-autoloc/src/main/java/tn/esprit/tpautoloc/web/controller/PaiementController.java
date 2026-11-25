package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IPaiementService;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements (Lecture seule)")
public class PaiementController {

    private final IPaiementService paiementService;

    @Operation(summary = "Lister tous les paiements (lecture seule)")
    @ApiResponse(responseCode = "200", description = "Liste des paiements récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<PaiementDTO>> findAll() {
        return ResponseEntity.ok(paiementService.findAllDto());
    }

    @Operation(summary = "Consulter un paiement par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaiementDTO> findById(@Parameter(description = "Identifiant du paiement") @PathVariable Long id) {
        return ResponseEntity.ok(paiementService.findByIdDto(id));
    }
}
