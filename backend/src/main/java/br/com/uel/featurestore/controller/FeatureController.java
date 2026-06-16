package br.com.uel.featurestore.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.uel.featurestore.model.Feature;
import br.com.uel.featurestore.service.FeatureService;

@RestController
@RequestMapping("/features")
public class FeatureController {
    private final FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @PostMapping
    public ResponseEntity<?> createFeature(@RequestBody Feature featureData) {
        try {
            featureService.createFeature(featureData);
            return ResponseEntity.status(HttpStatus.CREATED).body("Feature criada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao criar feature.");
        }
    }

    @GetMapping("/versao/{versionId}")
    public ResponseEntity<?> getFeaturesByVersionId(@PathVariable int versionId) {
        try {
            List<Feature> features = featureService.getFeaturesByVersionId(versionId);
            return ResponseEntity.ok(features);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao buscar features.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeatureById(@PathVariable int id) {
        try {
            Feature feature = featureService.getFeatureById(id);
            return ResponseEntity.ok(feature);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro interno ao buscar feature.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeature(@PathVariable int id, @RequestBody Feature featureData) {
        try {
            featureService.updateFeature(id, featureData);
            return ResponseEntity.ok("Feature atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao atualizar feature.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeature(@PathVariable int id) {
        try {
            featureService.deleteFeature(id);
            return ResponseEntity.ok("Feature excluída com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao excluir feature.");
        }
    }
}