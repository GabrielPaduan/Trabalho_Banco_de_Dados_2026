package br.com.uel.featurestore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uel.featurestore.model.DataFontDataset;
import br.com.uel.featurestore.service.DataFontDatasetService;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/fonteDadosDataset")
public class DataFontDatasetController {
    
    private final DataFontDatasetService dataFontDatasetService;

    public DataFontDatasetController(DataFontDatasetService dataFontDatasetService) {
        this.dataFontDatasetService = dataFontDatasetService;
    }

    @PostMapping
    public ResponseEntity<String> createNewRelation(@RequestBody DataFontDataset dataFontDataset) {
        try {
            dataFontDatasetService.createRelationDataFontDataset(dataFontDataset);
            return ResponseEntity.status(HttpStatus.CREATED).body("Relação criada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor!");
        }
    }
    
    @GetMapping("/dataset/{datasetId}")
    public ResponseEntity<?> getRelationByDatasetId(@PathVariable int datasetId) {
        try {
            List<DataFontDataset> listDataFontDataset = dataFontDatasetService.getRelationDataFontDatasetByDatasetId(datasetId);
            return ResponseEntity.ok().body(listDataFontDataset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor!");
        }
    }
    
    @GetMapping("/fonte/{dataFontId}")
    public ResponseEntity<?> getRelationByDataFontId(@PathVariable int dataFontId) {
        try {
            List<DataFontDataset> listDataFontId = dataFontDatasetService.getRelationDataFontDatasetByDataFontId(dataFontId);
            return ResponseEntity.ok().body(listDataFontId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor!");
        }
    }
}