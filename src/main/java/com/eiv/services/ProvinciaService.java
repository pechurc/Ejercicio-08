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

    @Autowired
    private ProvinciaRepository provinciaRepository;
    
    @Transactional(readOnly = true)
    public Optional<ProvinciaEntity> findById(Long id) {
        return provinciaRepository.findById(id);
    }
    
    @Transactional
    public ProvinciaEntity nueva(ProvinciaDto dto) {
        ProvinciaEntity nuevaProvincia = new ProvinciaEntity(dto.getId(), dto.getNombre());
        
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
                () -> new RuntimeException(String.format("No existe la provincia con id %s", id)));
        
        provinciaRepository.delete(provincia);
    }
}
