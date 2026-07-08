package br.com.uel.featurestore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import br.com.uel.featurestore.dao.AccessLogDAO;
import br.com.uel.featurestore.model.AccessLog;
import br.com.uel.featurestore.model.User;

@Service
public class AccessLogService {
    private final AccessLogDAO accessLogDAO;
    private final UserService userService;
    
    public AccessLogService(AccessLogDAO accessLogDAO, UserService userService) {
        this.accessLogDAO = accessLogDAO;
        this.userService = userService;
    }

    public void create(AccessLog accessLog) {
        if (accessLog.getDateTime() == null) {
            accessLog.setDateTime(LocalDateTime.now());
        }

        long randomDaysToSubtract = ThreadLocalRandom.current().nextLong(365);
        long randomHoursToSubtract = ThreadLocalRandom.current().nextLong(24);
        long randomMinutesToSubtract = ThreadLocalRandom.current().nextLong(60);

        LocalDateTime randomDateTime = LocalDateTime.now()
                .minusDays(randomDaysToSubtract)
                .minusHours(randomHoursToSubtract)
                .minusMinutes(randomMinutesToSubtract);

        accessLog.setDateTime(randomDateTime);

        User user = userService.getUserByEmail(accessLog.getUserCPF());
        accessLog.setUserCPF(user.getCpf());

        accessLogDAO.save(accessLog);
    }

    public AccessLog findById(Integer id) {
        return accessLogDAO.findById(id);
    }

    public List<AccessLog> findByUser(String userId) {
        User userData = userService.getUserByEmail(userId);
        if (userData == null) {
            throw new NoSuchElementException("Usuário não encontrado ou usuário não registrou nenhum log.");
        }
        return accessLogDAO.findByUser(userData.getCpf());
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
