package com.eiv.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eiv.dtos.ProvinciaDto;
import com.eiv.entities.ProvinciaEntity;
import com.eiv.repositories.ProvinciaRepository;

@Service
public class ProvinciaService {

    private ProvinciaRepository provinciaRepository;
    
    @Autowired
    public ProvinciaService(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }
    
    @Transactional(readOnly = true)
    public Optional<ProvinciaEntity> findById(Long id) {
        return provinciaRepository.findById(id);
    }
    
    @Transactional
    public ProvinciaEntity update(Long id, ProvinciaDto dto) {
        ProvinciaEntity provinciaEntity = findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("No existe la provincia con Id %s", id)));
        
        provinciaEntity.setNombre(dto.getNombre());
        
        provinciaRepository.save(provinciaEntity);
        
        return provinciaEntity;
    }
    
    @Transactional
    public ProvinciaEntity create(ProvinciaDto dto) {
        
        Long id = dto.getId();
        if (id == null) {
            id = provinciaRepository.maxId().orElse(0L) + 1;
        }

        ProvinciaEntity nuevaProvincia = new ProvinciaEntity(id, dto.getNombre());
        
        provinciaRepository.save(nuevaProvincia);
        
        return nuevaProvincia;
    }
    
    @Transactional
    public List<ProvinciaEntity> buscarTodas() {
        return provinciaRepository.findAll();
    }
    
    @Transactional
    public void borrar(Long id) {
        
        ProvinciaEntity provincia = provinciaRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("No existe la provincia con Id %s", id)));
        
        provinciaRepository.delete(provincia);
    }
}
