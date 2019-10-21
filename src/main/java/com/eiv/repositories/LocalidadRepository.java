package com.eiv.repositories;

import java.sql.SQLException;
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

import com.eiv.entities.LocalidadEntity;
import com.eiv.entities.ProvinciaEntity;

@Repository
public class LocalidadRepository implements CrudRepository<LocalidadEntity, Long> {
    
    private static final String SQL_FIND_BY_ID = "SELECT l.id, l.nombre, p.id as p_id, p.nombre"
            + " as p_nombre FROM localidades as l INNER JOIN provincias as p ON p.id=l.provinciaId"
            + " WHERE l.id=:id";
    private static final String SQL_FIND_BY_PROVINCIA = "SELECT l.id, l.nombre, p.id as p_id,"
            + " p.nombre as p_nombre FROM localidades as l INNER JOIN provincias as p"
            + " ON p.id=l.provinciaId"
            + " WHERE provinciaId=:provinciaId";
    private static final String SQL_FIND_ALL = "SELECT l.id, l.nombre, p.id as p_id, p.nombre"
            + " as p_nombre FROM localidades as l INNER JOIN provincias as p ON p.id=l.provinciaId";
    private static final String SQL_INSERT = "INSERT INTO localidades (id, nombre, provinciaId)"
            + " VALUES (:id, :nombre, :provinciaId);";
    private static final String SQL_UPDATE = "UPDATE localidades SET nombre=:nombre,"
            + " provinciaId=:provinciaId WHERE id=:id;";
    private static final String SQL_DELETE = "DELETE FROM localidades WHERE id=:id";
    private static final String SQL_MAX_ID = "SELECT MAX(id) FROM localidades;";
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private ProvinciaRepository provinciaRepository;
    
    @Autowired
    public LocalidadRepository(DataSource dataSource) throws SQLException {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);      
    }
    
    private final RowMapper<LocalidadEntity> rowMapper = (rs, row) -> {
        Long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        ProvinciaEntity provincia = new ProvinciaEntity();
        provincia.setId(rs.getLong("p_id"));
        provincia.setNombre(rs.getString("p_nombre"));
        return new LocalidadEntity(id, nombre, provincia);
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
        parameters.put("provinciaId", t.getProvincia().getId());
        
        if (optional.isPresent()) {
            LocalidadEntity localidad = optional.get();
            
            parameters.put("id", localidad.getId());        
            
            if (t.getProvincia() != localidad.getProvincia()) {
                provinciaRepository
                        .findById(localidad.getProvincia().getId())
                        .ifPresent(p -> {
                            localidad.setProvincia(p); 
                        });                
            }
            if (t.getNombre() != localidad.getNombre()) {
                localidad.setNombre(t.getNombre());
            }
            
            parameters.put("nombre", localidad.getNombre());
            parameters.put("provinciaId", localidad.getProvincia().getId());
            
            namedParameterJdbcTemplate.update(SQL_UPDATE, parameters);  
        } else {
            Long id = t.getId() == null ? maxId().orElse(0L) + 1L : t.getId();
            
            parameters.put("id", id); 
            namedParameterJdbcTemplate.update(SQL_INSERT, parameters);
        }
    }
    
    @Override
    public void delete(LocalidadEntity t) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", t.getId());
    
        namedParameterJdbcTemplate.queryForObject(SQL_DELETE, params, rowMapper);
    }
    
    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
