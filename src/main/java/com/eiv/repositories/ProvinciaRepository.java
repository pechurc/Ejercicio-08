package com.eiv.repositories;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eiv.entities.ProvinciaEntity;

@Repository
public class ProvinciaRepository implements CrudRepository<ProvinciaEntity, Long> {

    private static final String SQL_FIND_BY_ID = "SELECT * FROM provincias WHERE id=:id";
    private static final String SQL_FIND_ALL = "SELECT * FROM provincias";
    private static final String SQL_INSERT = "INSERT INTO provincias (id, nombre) "
            + "VALUES (:id, :nombre);";
    private static final String SQL_DELETE = "DELETR FROM provincias WHERE id=:id";
  
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    private final RowMapper<ProvinciaEntity> rowMapper = (rs, row) -> {
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        return new ProvinciaEntity(id, nombre);
    };
    
    @Autowired
    public ProvinciaRepository(DataSource dataSource) throws SQLException {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);   
    }
    
    @Override
    public Optional<ProvinciaEntity> findById(Long id) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
        
            ProvinciaEntity provinciaEntity = namedParameterJdbcTemplate.queryForObject(
                    SQL_FIND_BY_ID, params, rowMapper);
            
            return Optional.of(provinciaEntity);  
        } catch (DataAccessException e) {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public List<ProvinciaEntity> findAll() {

        List<ProvinciaEntity> resultados = namedParameterJdbcTemplate
                .query(SQL_FIND_ALL, rowMapper);
        
        return resultados;
    }

    @Override
    public void save(ProvinciaEntity t) {
          
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("id", t.getId());
        parameters.put("nombre", t.getNombre());

        namedParameterJdbcTemplate.update(SQL_INSERT, parameters);
    }
    
    @Override
    public void delete(Long id) {
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("id", id);

        namedParameterJdbcTemplate.update(SQL_DELETE, parameters);
    }
    
    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }
}
