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

import com.eiv.entities.LocalidadEntity;

@Repository
public class LocalidadRepository implements CrudRepository<LocalidadEntity, Long> {
    
    private static final String SQL_FIND_BY_ID = "SELECT * FROM localidades WHERE id=:id";
    private static final String SQL_FIND_BY_PROVINCIA = "SELECT * FROM localidades "
            + "WHERE provinciaId=:provinciaId";
    private static final String SQL_FIND_ALL = "SELECT * FROM localidades";
    private static final String SQL_INSERT = "INSERT INTO localidades (nombre, provinciaId)"
            + " VALUES (:nombre, :provinciaId);";
    private static final String SQL_UPDATE = "UPDATE localidades SET nombre=:nombre, "
            + "provinciaId=:provinciaId WHERE id=:id;";
    private static final String SQL_DELETE = "DELETE FROM localidades WHERE id=:id";
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private ProvinciaRepository provinciaRepository;
    
    @Autowired
    public LocalidadRepository(DataSource dataSource) throws SQLException {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);      
    }
    
    private final RowMapper<LocalidadEntity> rowMapper = (rs, row) -> {
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        long provinciaId = rs.getLong("provinciaId");
        return new LocalidadEntity(id, nombre, provinciaId);
    };
    
    @Override
    public Optional<LocalidadEntity> findById(Long id) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
        
            LocalidadEntity localidadEntity = namedParameterJdbcTemplate.queryForObject(
                    SQL_FIND_BY_ID, params, rowMapper);
            
            return Optional.of(localidadEntity);  
        } catch (DataAccessException e) {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public List<LocalidadEntity> findAll() {
        
        List<LocalidadEntity> resultados = namedParameterJdbcTemplate
                .query(SQL_FIND_ALL, rowMapper);
        
        return resultados;
    }

    public List<LocalidadEntity> findAllByProvincia(Long provinciaId) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("provinciaId", provinciaId);
        
        List<LocalidadEntity> resultados = namedParameterJdbcTemplate
                .query(SQL_FIND_BY_PROVINCIA, params, rowMapper);
        
        return resultados;
    }
    
    @Override
    public void save(LocalidadEntity t) {
        
        Optional<LocalidadEntity> optional = findById(t.getId());        
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("nombre", t.getNombre());
        parameters.put("provinciaId", t.getProvinciaId());
        
        if (optional.isPresent()) {
            LocalidadEntity localidad = optional.get();
            
            parameters.put("id", localidad.getId());        
            
            if (t.getProvinciaId() != localidad.getProvinciaId()) {
                provinciaRepository
                        .findById(localidad.getProvinciaId())
                        .ifPresent(p -> {
                            localidad.setProvinciaId(p.getId()); 
                        });                
            }
            if (t.getNombre() != localidad.getNombre()) {
                localidad.setNombre(t.getNombre());
            }
            
            parameters.put("nombre", localidad.getNombre());
            parameters.put("provinciaId", localidad.getProvinciaId());
            
            namedParameterJdbcTemplate.update(SQL_UPDATE, parameters);  
        } else {
            parameters.put("nombre", t.getNombre());
            parameters.put("provinciaId", t.getProvinciaId());
            namedParameterJdbcTemplate.update(SQL_INSERT, parameters);
        }
    }
    
    @Override
    public void delete(Long id) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
    
        namedParameterJdbcTemplate.queryForObject(SQL_DELETE, params, rowMapper);
    }
    
    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
