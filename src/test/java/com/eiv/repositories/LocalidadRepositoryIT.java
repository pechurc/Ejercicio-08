package com.eiv.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.eiv.entities.LocalidadEntity;
import com.eiv.entities.ProvinciaEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LocalidadRepositoryIT.TestCfg.class)
public class LocalidadRepositoryIT {

    @Configuration
    @EnableTransactionManagement
    public static class TestCfg {
        
        @Bean
        public DataSource getDataSource() {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setUrl("jdbc:h2:mem:testdb"
                    + ";INIT=runscript from 'src/test/resources/test-localidades.sql'");
            ds.setUser("sa");
            return ds;
        }
        
        @Bean
        public PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(getDataSource());
        }
        
        @Bean
        public ProvinciaRepository getProvinciaRepository() {
            return new ProvinciaRepository(getDataSource());
        }
    }
    
    @Autowired DataSource dataSource;
    @Autowired ProvinciaRepository provinciaRepository;
    
    @Test
    public void whenLocalidadId1_thenIsPresent() {
        
        LocalidadRepository localidadRepository = new LocalidadRepository(dataSource);
        localidadRepository.setProvinciaRepository(provinciaRepository);
        
        Optional<LocalidadEntity> optional = localidadRepository.findById(1L);
        
        assertThat(optional).isPresent();
    }
    
    @Test
    public void whenLocalidadId10_thenIsNotPresent() {
        
        LocalidadRepository localidadRepository = new LocalidadRepository(dataSource);
        localidadRepository.setProvinciaRepository(provinciaRepository);
        
        Optional<LocalidadEntity> optional = localidadRepository.findById(10L);
        
        assertThat(optional).isNotPresent();
    }
    
    @Test
    public void givenAllLocalidades_whenFindById_thenFindProvincia() {
        
        LocalidadRepository localidadRepository = new LocalidadRepository(dataSource);
        localidadRepository.setProvinciaRepository(provinciaRepository);
        List<LocalidadEntity> localidadEntities = localidadRepository.findAll();
        
        assertThat(localidadEntities).hasSize(3);
        
        localidadEntities.forEach(item -> {
            
            Optional<LocalidadEntity> optional = localidadRepository.findById(item.getId());
            
            assertThat(optional)
                    .contains(item);
        });
    }
    
    @Test
    @Transactional
    public void givenNewLocalidad_whenId_thenSave() {
        
        LocalidadRepository localidadRepository = new LocalidadRepository(dataSource);
        Optional<ProvinciaEntity> provincia = provinciaRepository.findById(1L);
        assertThat(provincia).isPresent();
        
        LocalidadEntity localidad = new LocalidadEntity(10L, "localidadTEST", provincia.get());
        

        localidadRepository.save(localidad);
        
        Optional<LocalidadEntity> optional = localidadRepository.findById(10L);
        assertThat(optional).contains(localidad);
    }
    
    @Test
    @Transactional
    public void givenNewLocalidad_whenNoId_thenSave() {
        
        LocalidadRepository localidadRepository = new LocalidadRepository(dataSource);
        Optional<ProvinciaEntity> provincia = provinciaRepository.findById(1L);
        assertThat(provincia).isPresent();
        
        LocalidadEntity localidad = new LocalidadEntity(null, "localidadTEST", provincia.get());
        

        localidadRepository.save(localidad);
        
        Optional<LocalidadEntity> optional = localidadRepository.findById(localidad.getId());
        assertThat(optional).contains(localidad);
    }
    
    @Test
    @Transactional
    public void givenExistLocalidad_thenUpdate() {
        
        LocalidadRepository localidadRepository = new LocalidadRepository(dataSource);
        localidadRepository.setProvinciaRepository(provinciaRepository);
        
        Optional<LocalidadEntity> optional = localidadRepository.findById(1L);
        assertThat(optional).isPresent();
        
        LocalidadEntity localidad = optional.get();
        
        localidad.setNombre("TEST");        

        localidadRepository.save(localidad);
        
        Optional<LocalidadEntity> saved = localidadRepository.findById(localidad.getId());
        assertThat(saved).contains(localidad);
    }
    
    @Test
    @Transactional
    public void givenExistLocalidad_thenDelete() {
        
        LocalidadRepository localidadRepository = new LocalidadRepository(dataSource);
        
        localidadRepository.findById(1L).ifPresent(l -> {
            localidadRepository.delete(l);
        });
        
        Optional<LocalidadEntity> optional = localidadRepository.findById(1L);
        assertThat(optional).isNotPresent();
    }
}
