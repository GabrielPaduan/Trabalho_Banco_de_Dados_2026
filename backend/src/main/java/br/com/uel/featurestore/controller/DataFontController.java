package br.com.uel.featurestore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uel.featurestore.model.DataFont;
import br.com.uel.featurestore.service.DataFontService;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<?> createDataFont (@RequestBody DataFont data) {
        try {
            DataFont dataFont = dataFontService.createDataFont(data);
            return ResponseEntity.status(HttpStatus.CREATED).body(dataFont); // Retorna 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Alterado para 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getDataFontList() {
        try {
            List<DataFont> dataFontList = dataFontService.getDataFont();
            return ResponseEntity.ok(dataFontList);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Padronizado com HttpStatus
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}