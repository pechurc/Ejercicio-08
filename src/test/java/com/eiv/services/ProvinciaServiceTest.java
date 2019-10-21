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

import com.eiv.dtos.ProvinciaDtoImpl;
import com.eiv.entities.ProvinciaEntity;
import com.eiv.repositories.ProvinciaRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProvinciaServiceTest {

    @Mock
    ProvinciaRepository provinciaRepository;

    @InjectMocks
    ProvinciaService provinciaService;

    @Test
    public void givenProvinciaDto_whenCreate_thenProvinciaEntityCreated() {
        
        Mockito.when(provinciaRepository.maxId()).thenReturn(Optional.of(0L));
        
        ProvinciaDtoImpl provincia = new ProvinciaDtoImpl("TEST");
        
        ProvinciaEntity provinciaEntity = provinciaService.create(provincia);
        
        assertThat(provinciaEntity.getId()).isEqualTo(1L);
        assertThat(provinciaEntity.getNombre()).isEqualTo("TEST");
    }
    
    @Test
    public void givenProvinciaDto_whenUpdate_thenProvinciaEntityUpdated() {
        
        Mockito.when(provinciaRepository
                .findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new ProvinciaEntity(0L, "ORIGINAL")));
        
        ProvinciaDtoImpl provincia = new ProvinciaDtoImpl("TEST");
        
        ProvinciaEntity provinciaEntity = provinciaService.update(0L, provincia);

        assertThat(provinciaEntity.getId()).isEqualTo(0L);
        assertThat(provinciaEntity.getNombre()).isEqualTo("TEST");
    }
    
    @Test
    public void givenProvinciaDto_whenUpdateNonExist_thenThrowException() {
        
        ProvinciaDtoImpl provincia = new ProvinciaDtoImpl("TEST");
        
        Throwable throwable = catchThrowable(() -> provinciaService.update(0L, provincia));

        assertThat(throwable)
        .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No existe la provincia con Id 0");
    }

    @Test
    public void givenProvinciaId_thenDelete() {

        ProvinciaEntity provinciaEntity = new ProvinciaEntity(0L, "TEST");
        
        Mockito.when(provinciaRepository
                .findById(Mockito.eq(0L)))
                .thenReturn(Optional.of(provinciaEntity));
        
        provinciaService.borrar(0L);
        
        Mockito.verify(provinciaRepository).findById(Mockito.eq(0L));
        Mockito.verify(provinciaRepository).delete(Mockito.eq(provinciaEntity));
    }

    @Test
    public void givenProvinciaId_whenDeleteNonExist_thenThrowException() {

        Throwable throwable = catchThrowable(() -> provinciaService.borrar(0L));
        
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe la provincia con Id 0");

        Mockito.verify(provinciaRepository).findById(Mockito.eq(0L));
        Mockito.verify(provinciaRepository, never()).delete(Mockito.any(ProvinciaEntity.class));
    }
}
