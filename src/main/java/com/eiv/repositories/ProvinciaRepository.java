package com.eiv.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private static final String SQL_UPDATE = "UPDATE provincias set nombre=:nombre WHERE "
            + "id=:id;";
    private static final String SQL_DELETE = "DELETE FROM provincias WHERE id=:id";
    private static final String SQL_MAX_ID = "SELECT MAX(id) FROM provincias;";
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    private final RowMapper<ProvinciaEntity> rowMapper = (rs, row) -> {
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        return new ProvinciaEntity(id, nombre);
    };
    
    @Autowired
    public ProvinciaRepository(DataSource dataSource) {
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
          
        Optional<ProvinciaEntity> optional = findById(t.getId());
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("nombre", t.getNombre());
        
        if (optional.isPresent()) {
            ProvinciaEntity provincia = optional.get();
            
            if (provincia.getNombre() != t.getNombre()) {
                provincia.setNombre(t.getNombre());
            }
            
            parameters.put("id", provincia.getId());
            
            namedParameterJdbcTemplate.update(SQL_UPDATE, parameters);
        } else {
            Long id = t.getId() == null ? maxId().orElse(0L) + 1L : t.getId();
            t.setId(id);
            parameters.put("id", id); 

            namedParameterJdbcTemplate.update(SQL_INSERT, parameters);
        }
    }
    
    @Override
    public void delete(ProvinciaEntity entity) {
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("id", entity.getId());

        namedParameterJdbcTemplate.update(SQL_DELETE, parameters);
    }
    
    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Optional<Long> maxId() {

        JdbcTemplate jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        Long maxId = jdbcTemplate.queryForObject(SQL_MAX_ID, Long.class);
        
        if (maxId == null) {
            return Optional.empty();
        } else {
            return Optional.of(maxId);
        }
    }
}
