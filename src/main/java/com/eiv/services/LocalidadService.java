package com.eiv.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eiv.dtos.LocalidadDto;
import com.eiv.entities.LocalidadEntity;
import com.eiv.entities.ProvinciaEntity;
import com.eiv.repositories.LocalidadRepository;

@Service
public class LocalidadService {

    @Autowired
    private ProvinciaService provinciaService;
    @Autowired
    private LocalidadRepository localidadRepository;
    
    public Optional<LocalidadEntity> findById(Long id) {
        return localidadRepository.findById(id);
    }
    
    public List<LocalidadEntity> buscarTodas() {
        return localidadRepository.findAll();
    }
    
    public List<LocalidadEntity> buscarPorProvincia(Long provinciaId) {
        provinciaService.findById(provinciaId).orElseThrow(
                () -> new RuntimeException("Provincia no encontrada"));
        
        return localidadRepository.findAllByProvincia(provinciaId);
    }
    
    public LocalidadEntity create(LocalidadDto dto) {
        
        Long id = dto.getId();
        if (id == null) {
            id = localidadRepository.maxId().orElse(0L) + 1;
        }

        ProvinciaEntity provincia = provinciaService.findById(dto.getProvinciaId()).orElseThrow(
                () -> new RuntimeException("Provincia no encontrada"));
        
        LocalidadEntity nuevaLocalidad = new LocalidadEntity(id, dto.getNombre(), provincia);
        
        localidadRepository.save(nuevaLocalidad);
        
        return nuevaLocalidad;
    }
    
    public LocalidadEntity update(Long id, LocalidadDto dto) {
        
        LocalidadEntity localidadEntity = findById(id).orElseThrow(
                () -> new RuntimeException("Localidad no encontrada"));
        ProvinciaEntity provincia = provinciaService.findById(dto.getProvinciaId()).orElseThrow(
                () -> new RuntimeException("Provincia no encontrada"));
        
        localidadEntity.setNombre(dto.getNombre());
        localidadEntity.setProvincia(provincia);
        
        localidadRepository.save(localidadEntity);
        
        return localidadEntity;
    }
    
    public void borrar(Long id) {
        
        LocalidadEntity localidad = localidadRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Localidad no encontrada"));
        
        localidadRepository.delete(localidad);
    }
}
