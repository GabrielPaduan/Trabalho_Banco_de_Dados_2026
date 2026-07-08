package br.com.uel.featurestore.controller;

import java.util.List;
import java.util.Map;
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

import br.com.uel.featurestore.model.AccessLog;
import br.com.uel.featurestore.service.AccessLogService;

@RestController
@RequestMapping("/logsAcesso")
public class AccessLogController {
    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccessLog accessLog) {
        accessLogService.create(accessLog);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            AccessLog log = accessLogService.findById(id);
            return ResponseEntity.ok(log);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } 
    }

    @GetMapping("dataset/{userId}")
    public ResponseEntity<?> findByDatasetId(@PathVariable String userId) {
        try {
            List<AccessLog> logs = accessLogService.findByUser(userId);
            return ResponseEntity.ok(logs);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } 
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<AccessLog> logs = accessLogService.findAll();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AccessLog accessLog) {
        try {
            AccessLog updatedLog = accessLogService.update(id, accessLog);
            return ResponseEntity.ok(updatedLog); 
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        boolean deleted = accessLogService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }
}
