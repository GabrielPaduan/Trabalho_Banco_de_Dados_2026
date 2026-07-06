package br.com.uel.featurestore.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import br.com.uel.featurestore.model.Version;
import br.com.uel.featurestore.service.VersionService;

@RestController
@RequestMapping("/versoes")
public class VersionController {
    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNewVersion(@RequestParam("datasetId") int datasetId,
            @RequestParam("archivePath") MultipartFile archivePath) {
        try {
            // Verificação preventiva de integridade do payload de rede
            if (archivePath.isEmpty()) {
                return ResponseEntity.badRequest().body("É obrigatório anexar um arquivo válido!");
            }

            // Centraliza os metadados textuais recebidos dentro do modelo básico
            Version versionData = new Version();
            versionData.setDatasetId(datasetId);

            // Delega o processamento de armazenamento físico e lógico ao Service
            Version newVersion = versionService.createNewVersion(versionData, archivePath);
            return ResponseEntity.status(HttpStatus.CREATED).body(newVersion);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Falha operacional no processamento do arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/{id}") 
    public ResponseEntity<?> getVersion(@PathVariable int id) {
        try {
            Version version = versionService.getVersionById(id);
            return ResponseEntity.ok().body(version); 
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); 
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @GetMapping("/baseVersion/{id}") 
    public ResponseEntity<?> getBaseVersion(@PathVariable int id) {
        try {
            Version version = versionService.getLastVersion(id);
            return ResponseEntity.ok().body(version); 
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); 
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @GetMapping("/dataset/{datasetId}") 
    public ResponseEntity<?> getListVersion(@PathVariable int datasetId) {
        try {
            List<Version> listVersions = versionService.getListVersion(datasetId);
            return ResponseEntity.ok().body(listVersions); 
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); 
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadVersionFile(@PathVariable int id) {
        try {
            Version version = versionService.getVersionById(id);

            Path caminhoArquivo = Paths.get(version.getArchivePath());
            Resource resource = new UrlResource(caminhoArquivo.toUri());

            // 3. Verificamos se o arquivo realmente ainda existe naquela pasta
            if (resource.exists() || resource.isReadable()) {
                
                // 4. Montamos a resposta forçando o navegador a fazer o Download (attachment)
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dataset_v" + version.getNumVersion() + ".csv\"") // Se quiser, pode extrair a extensão real do archivePath
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}