package br.com.uel.featurestore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uel.featurestore.model.DataFont;
import br.com.uel.featurestore.service.DataFontService;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/fonteDados")
public class DataFontController {
    private final DataFontService dataFontService;

    public DataFontController(DataFontService dataFontService) {
        this.dataFontService = dataFontService;
    }

    @PostMapping("/criar")
    public ResponseEntity<String> createDataFont (@RequestBody DataFont data) {
        try {
            dataFontService.createDataFont(data);
            return ResponseEntity.ok().body("Fonte de dados cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
    
    @GetMapping("/listar")
    public ResponseEntity<?> getDataFontList() {
        try {
            List<DataFont> dataFontList = dataFontService.getDataFont();
            return ResponseEntity.ok().body(dataFontList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor!" + e.getMessage());
        }
    }
}
