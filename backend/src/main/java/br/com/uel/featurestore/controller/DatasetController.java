package br.com.uel.featurestore.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uel.featurestore.model.Dataset;
import br.com.uel.featurestore.service.DatasetService;

@RestController
@RequestMapping("/datasets")
public class DatasetController {
    
    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @PostMapping
    public ResponseEntity<?> criarDataset(@RequestBody Dataset dataset) {
        try {
            Dataset datasetReturn = datasetService.inserirDatasetBanco(dataset);
            return ResponseEntity.status(HttpStatus.CREATED).body(datasetReturn);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro no servidor: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listDatasetById(@PathVariable Integer id) {
        try {
            Dataset dataset = datasetService.listDatasetById(id);
            return ResponseEntity.ok(dataset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<?> listUserDataset(@PathVariable String email) {
        try {
            List<Dataset> datasets = datasetService.listDatasets(email);
            return ResponseEntity.ok(datasets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivateDataset(@PathVariable Integer id) {
        try {
            datasetService.desactiveDataset(id);
            return ResponseEntity.ok("Dataset excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDataset(@RequestBody Dataset dataset) {
        try {
            Dataset responseDataset = datasetService.updateDatset(dataset);
            return ResponseEntity.ok(responseDataset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor " + e.getMessage());
        }
    }
}