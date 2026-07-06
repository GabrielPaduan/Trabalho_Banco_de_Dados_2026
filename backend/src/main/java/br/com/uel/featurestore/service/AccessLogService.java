package br.com.uel.featurestore.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.uel.featurestore.dao.AccessLogDAO;
import br.com.uel.featurestore.model.AccessLog;

@Service
public class AccessLogService {
    private final AccessLogDAO accessLogDAO;
    
    public AccessLogService(AccessLogDAO accessLogDAO) {
        this.accessLogDAO = accessLogDAO;
    }

    public void create(AccessLog accessLog) {
        if (accessLog.getDateTime() == null) {
            accessLog.setDateTime(LocalDateTime.now());
        }
        accessLogDAO.save(accessLog);
    }

    public AccessLog findById(Integer id) {
        return accessLogDAO.findById(id);
    }

    public List<AccessLog> findAll() {
        return accessLogDAO.findAll();
    }

    public AccessLog update(Integer id, AccessLog accessLog) {
        AccessLog existingLog = accessLogDAO.findById(id);
        if (existingLog == null) {
            return null;
        }
        accessLog.setId(id);
        accessLogDAO.update(accessLog);
        return accessLog;
    }

    public boolean delete(Integer id) {
        int rowsAffected = accessLogDAO.delete(id);
        return rowsAffected > 0;
    }

}
