package com.eiv.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.never;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.eiv.dtos.LocalidadDtoImpl;
import com.eiv.entities.LocalidadEntity;
import com.eiv.entities.ProvinciaEntity;
import com.eiv.repositories.LocalidadRepository;

@RunWith(MockitoJUnitRunner.class)
public class LocalidadServiceTest {
    
    @Mock 
    ProvinciaService provinciaService;
    
    @Mock     
    LocalidadRepository localidadRepository;
    
    @InjectMocks    
    LocalidadService localidadService;
    
    @Test
    public void givenLocalidadDto_whenCreate_thenLocalidadEntityCreated() {
        
        Mockito.when(provinciaService.findById(1L))
            .thenReturn(Optional.of(new ProvinciaEntity(1L, "ProvinciaTest")));
        
        Mockito.when(localidadRepository.maxId()).thenReturn(Optional.of(0L));
        
        LocalidadDtoImpl localidad = new LocalidadDtoImpl("TEST", 1L);
        
        LocalidadEntity localidadEntity = localidadService.create(localidad);
        
        assertThat(localidadEntity.getId()).isEqualTo(1L);
        assertThat(localidadEntity.getNombre()).isEqualTo("TEST");
        assertThat(localidadEntity.getProvincia().getNombre()).isEqualTo("ProvinciaTest");
    }
    
    @Test
    public void givenLocalidadDto_whenUpdate_thenLocalidadEntityUpdated() {
        
        Mockito.when(provinciaService.findById(1L))
            .thenReturn(Optional.of(new ProvinciaEntity(1L, "ProvinciaTest")));
        
        Mockito.when(localidadRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new LocalidadEntity(0L, "ORIGINAL",
                        new ProvinciaEntity(1L, "ProvinciaTest"))));
        
        LocalidadDtoImpl localidad = new LocalidadDtoImpl("TEST", 1L);
        
        LocalidadEntity localidadEntity = localidadService.update(0L, localidad);
        
        assertThat(localidadEntity.getId()).isEqualTo(0L);
        assertThat(localidadEntity.getNombre()).isEqualTo("TEST");
        assertThat(localidadEntity.getProvincia().getNombre()).isEqualTo("ProvinciaTest");
    }
    
    @Test
    public void givenLocalidadDto_whenUpdateNonExist_thenThrowException() {
        
        LocalidadDtoImpl localidad = new LocalidadDtoImpl("TEST", 1L);
        
        Throwable throwable = catchThrowable(() -> localidadService.update(0L, localidad));

        assertThat(throwable)
        .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Localidad no encontrada");
    }
    
    @Test
    public void givenProvinciaId_thenDelete() {

        LocalidadEntity localidadEntity = new LocalidadEntity(0L, "TEST", 
                new ProvinciaEntity(1L, "ProvinciaTest"));
        
        Mockito.when(localidadRepository
                .findById(Mockito.eq(0L)))
                .thenReturn(Optional.of(localidadEntity));
        
        localidadService.borrar(0L);
        
        Mockito.verify(localidadRepository).findById(Mockito.eq(0L));
        Mockito.verify(localidadRepository).delete(Mockito.eq(localidadEntity));
    }

    @Test
    public void givenProvinciaId_whenDeleteNonExist_thenThrowException() {

        Throwable throwable = catchThrowable(() -> localidadService.borrar(0L));
        
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Localidad no encontrada");

        Mockito.verify(localidadRepository).findById(Mockito.eq(0L));
        Mockito.verify(localidadRepository, never()).delete(Mockito.any(LocalidadEntity.class));
    }
}
