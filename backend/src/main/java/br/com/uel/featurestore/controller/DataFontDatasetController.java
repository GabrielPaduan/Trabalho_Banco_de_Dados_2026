package br.com.uel.featurestore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uel.featurestore.model.DataFontDataset;
import br.com.uel.featurestore.service.DataFontDatasetService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/fonteDadosDataset")
public class DataFontDatasetController {
    private final DataFontDatasetService dataFontDatasetService;

    public DataFontDatasetController(DataFontDatasetService dataFontDatasetService) {
        this.dataFontDatasetService = dataFontDatasetService;
    }

    @PostMapping("/criar")
    public ResponseEntity<String> createNewRelation(@RequestBody DataFontDataset dataFontDataset) {
        try {
            dataFontDatasetService.createRelationDataFontDataset(dataFontDataset);
            return ResponseEntity.ok().body("Busca realizada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor!");
        }
    }
    
    @PostMapping("/listarPorDataset")
    public ResponseEntity<?> getRelationByDatasetId(@RequestBody Map<String, Integer> datasetId) {
        try {
            int id = datasetId.get("datasetId");
            List<DataFontDataset> listDataFontDataset = dataFontDatasetService.getRelationDataFontDatasetByDatasetId(id);
            return ResponseEntity.ok().body(listDataFontDataset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor!");
        }
    }
    
    @PostMapping("/listarPorDataFont")
    public ResponseEntity<?> getRelationByDataFontId(@RequestBody Map<String, Integer> dataFontId) {
        try {
            int id = dataFontId.get("dataFontId");
            List<DataFontDataset> listDataFontId = dataFontDatasetService.getRelationDataFontDatasetByDataFontId(id);
            return ResponseEntity.ok().body(listDataFontId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor!");
        }
    }
}
