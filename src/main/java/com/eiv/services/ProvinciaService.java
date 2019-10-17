package com.eiv.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eiv.dtos.ProvinciaDto;
import com.eiv.entities.ProvinciaEntity;
import com.eiv.repositories.ProvinciaRepository;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;
    
    public Optional<ProvinciaEntity> findById(Long id) {
        return provinciaRepository.findById(id);
    }
    
    public ProvinciaEntity nueva(ProvinciaDto dto) {
        ProvinciaEntity nuevaProvincia = new ProvinciaEntity(dto.getId(), dto.getNombre());
        
        provinciaRepository.save(nuevaProvincia);
        
        return nuevaProvincia;
    }
    
    public List<ProvinciaEntity> buscarTodas() {
        return provinciaRepository.findAll();
    }
    
    public void borrar(Long id) {
        provinciaRepository.delete(id);
    }
}