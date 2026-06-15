package br.com.uel.featurestore.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uel.featurestore.model.Version;
import br.com.uel.featurestore.service.VersionService;

@RestController
@RequestMapping("/versoes")
public class VersionController {
    
    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @PostMapping // Opcional: remover o "/criar", a rota base POST /versoes já indica criação
    public ResponseEntity<?> createNewVersion(@RequestBody Version versionData) {
        try {
            Version newVersion = versionService.createNewVersion(versionData);
            // Padrão ouro para criação é o Status 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(newVersion);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    // Usando GET e passando o ID na própria URL: /versoes/dataset/5
    @GetMapping("/dataset/{datasetId}") 
    public ResponseEntity<?> getListVersion(@PathVariable int datasetId) {
        try {
            List<Version> listVersions = versionService.getListVersion(datasetId);
            return ResponseEntity.ok().body(listVersions); // 200
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}