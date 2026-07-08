package br.com.uel.featurestore.dao;

import br.com.uel.featurestore.model.AccessLog;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccessLogDAO {
    private final JdbcTemplate jdbcTemplate;

    public AccessLogDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(AccessLog accessLog) {
        String sql = "INSERT INTO feature_store.log_acesso (tipo_operacao, data_hora, cpf_usuario, id_dataset) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                accessLog.getOperationType(),
                accessLog.getDateTime(),
                accessLog.getUserCPF(),
                accessLog.getDatasetID()
        );
    }

    public AccessLog findById(Integer id) {
        String sql = "SELECT * FROM feature_store.log_acesso WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                AccessLog log = new AccessLog();
                log.setId(rs.getInt("id"));
                log.setOperationType(rs.getInt("tipo_operacao"));
                if (rs.getTimestamp("data_hora") != null) {
                    log.setDateTime(rs.getTimestamp("data_hora").toLocalDateTime());
                }              
                log.setUserCPF(rs.getString("cpf_usuario"));
                log.setDatasetID(rs.getInt("id_dataset"));
                return log;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<AccessLog> findByUser(String userCPF) {
        String sql = "SELECT id, tipo_operacao, data_hora, id_dataset FROM feature_store.log_acesso WHERE cpf_usuario = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                AccessLog log = new AccessLog();
                log.setId(rs.getInt("id"));
                log.setOperationType(rs.getInt("tipo_operacao"));
                if (rs.getTimestamp("data_hora") != null) {
                    log.setDateTime(rs.getTimestamp("data_hora").toLocalDateTime());
                }              
                log.setUserCPF(userCPF);
                log.setDatasetID(rs.getInt("id_dataset"));
                return log;
            }, userCPF);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<AccessLog> findAll() {
        String sql = "SELECT * FROM feature_store.log_acesso";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            AccessLog log = new AccessLog();
            log.setId(rs.getInt("id"));
            log.setOperationType(rs.getInt("tipo_operacao"));
            if (rs.getTimestamp("data_hora") != null) {
                log.setDateTime(rs.getTimestamp("data_hora").toLocalDateTime());
            }              
            log.setUserCPF(rs.getString("cpf_usuario"));
            log.setDatasetID(rs.getInt("id_dataset"));
            return log;
        });
    }

    public int update(AccessLog accessLog) {
        String sql = "UPDATE feature_store.log_acesso SET tipo_operacao = ?, data_hora = ?, cpf_usuario = ?, id_dataset = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                accessLog.getOperationType(),
                accessLog.getDateTime(),
                accessLog.getUserCPF(),
                accessLog.getDatasetID(),
                accessLog.getId()
        );
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM feature_store.log_acesso WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}