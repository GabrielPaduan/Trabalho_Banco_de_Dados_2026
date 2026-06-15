package br.com.uel.featurestore.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uel.featurestore.model.Dataset;
import br.com.uel.featurestore.service.DatasetService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/datasets")
public class DatasetController {
    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @PostMapping("/inserir")
    public ResponseEntity<?> criarDataset(@RequestBody Dataset dataset) {
        try {
            Dataset datasetReturn = datasetService.inserirDatasetBanco(dataset);
            return ResponseEntity.status(201).body(datasetReturn);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro no servidor: " + e.getMessage());
        }
    }

    @PostMapping("/listarNome")
    public ResponseEntity<?> listDatasetById(@RequestBody Map<String, Integer> datasetId) {
        try {
            Integer id = datasetId.get("datasetId");
            Dataset dataset = datasetService.listDatasetById(id);
            return ResponseEntity.status(200).body(dataset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @PostMapping("/listar")
    public ResponseEntity<?> listUserDataset(@RequestBody Map<String, String> userEmail) {
        try {
            String email = userEmail.get("userEmail");
            List<Dataset> datasets = datasetService.listDatasets(email);
            return ResponseEntity.status(200).body(datasets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<String> desactivateDataset(@RequestBody Map<String, Integer> identificador) {
        try {
            Integer id = identificador.get("id");
            System.out.println("Id: " + id);
            datasetService.desactiveDataset(id);
            return ResponseEntity.status(200).body("Dataset excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> updateDataset(@RequestBody Dataset dataset) {
        try {
            Dataset responseDataset = datasetService.updateDatset(dataset);
            return ResponseEntity.status(201).body(responseDataset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor " + e.getMessage());
        }
    }
}
