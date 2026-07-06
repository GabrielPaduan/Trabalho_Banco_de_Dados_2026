package br.com.uel.featurestore.dao;

import br.com.uel.featurestore.model.AccessLog;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccessLogDAO {
    private final JdbcTemplate jdbcTemplate;

    public AccessLogDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(AccessLog accessLog) {
        String sql = "INSERT INTO access_log (operation_type, date_time, user_cpf, dataset_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                accessLog.getOperationType(),
                accessLog.getDateTime(),
                accessLog.getUserCPF(),
                accessLog.getDatasetID()
        );
    }

    public AccessLog findById(Integer id) {
        String sql = "SELECT * FROM access_log WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                AccessLog log = new AccessLog();
                log.setId(rs.getInt("id"));
                log.setOperationType(rs.getInt("operation_type"));
                if (rs.getTimestamp("date_time") != null) {
                    log.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                }              
                log.setUserCPF(rs.getString("user_cpf"));
                log.setDatasetID(rs.getInt("dataset_id"));
                return log;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<AccessLog> findAll() {
        String sql = "SELECT * FROM access_log";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            AccessLog log = new AccessLog();
            log.setId(rs.getInt("id"));
            log.setOperationType(rs.getInt("operation_type"));
            if (rs.getTimestamp("date_time") != null) {
                log.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
            }              
            log.setUserCPF(rs.getString("user_cpf"));
            log.setDatasetID(rs.getInt("dataset_id"));
            return log;
        });
    }

    public int update(AccessLog accessLog) {
        String sql = "UPDATE access_log SET operation_type = ?, date_time = ?, user_cpf = ?, dataset_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                accessLog.getOperationType(),
                accessLog.getDateTime(),
                accessLog.getUserCPF(),
                accessLog.getDatasetID(),
                accessLog.getId()
        );
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM access_log WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}