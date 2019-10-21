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

import com.eiv.entities.ProvinciaEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProvinciaRepositoryIT.TestCfg.class)
public class ProvinciaRepositoryIT {
    
    @Configuration
    @EnableTransactionManagement
    public static class TestCfg {
        
        @Bean
        public DataSource getDataSource() {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setUrl("jdbc:h2:mem:testdb"
                    + ";INIT=runscript from 'src/test/resources/test-provincias.sql'");
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
    public void whenProvinciaId1_thenIsPresent() {
        
        ProvinciaRepository provinciaRepository = new ProvinciaRepository(dataSource);
        Optional<ProvinciaEntity> optional = provinciaRepository.findById(1L);
        
        assertThat(optional).isPresent();
    }
    
    @Test
    public void whenProvinciaId10_thenIsPresent() {
        
        ProvinciaRepository provinciaRepository = new ProvinciaRepository(dataSource);
        Optional<ProvinciaEntity> optional = provinciaRepository.findById(10L);
        
        assertThat(optional).isNotPresent();
    }
    
    
    @Test
    public void givenAllProvincias_whenFindById_thenFindProvincia() {
        
        ProvinciaRepository provinciaRepository = new ProvinciaRepository(dataSource);
        List<ProvinciaEntity> provinciaEntities = provinciaRepository.findAll();
        
        assertThat(provinciaEntities).hasSize(5);
        
        provinciaEntities.forEach(item -> {
            
            Optional<ProvinciaEntity> optional = provinciaRepository.findById(item.getId());
            
            assertThat(optional)
                    .contains(item);
        });
    }
    
    @Test
    @Transactional
    public void givenNewProvincia_whenId_thenSave() {
        
        ProvinciaEntity provincia = new ProvinciaEntity(10L, "ProvinciaTEST");
        
        ProvinciaRepository provinciaRepository = new ProvinciaRepository(dataSource);

        provinciaRepository.save(provincia);
        
        Optional<ProvinciaEntity> optional = provinciaRepository.findById(10L);
        assertThat(optional).contains(provincia);
    }
    
    @Test
    @Transactional
    public void givenNewProvincia_whenNoId_thenSave() {
        
        ProvinciaEntity provincia = new ProvinciaEntity(null, "ProvinciaTEST");
        
        ProvinciaRepository provinciaRepository = new ProvinciaRepository(dataSource);

        provinciaRepository.save(provincia);
        
        Optional<ProvinciaEntity> optional = provinciaRepository.findById(provincia.getId());
        assertThat(optional).contains(provincia);
    }
    
    @Test
    @Transactional
    public void givenExistProvincia_thenUpdate() {

        ProvinciaRepository provinciaRepository = new ProvinciaRepository(dataSource);
        
        Optional<ProvinciaEntity> optional = provinciaRepository.findById(1L);
        assertThat(optional).isPresent();
        
        ProvinciaEntity provincia = optional.get();
        
        provincia.setNombre("TEST");
        provinciaRepository.save(provincia);
        
        Optional<ProvinciaEntity> saved = provinciaRepository.findById(1L);
        assertThat(saved).isPresent().contains(provincia);
        assertThat(saved.get().getNombre()).isEqualTo(provincia.getNombre());
    }
    
    @Test
    @Transactional
    public void givenProvincia_thenDelete() {
        
        ProvinciaRepository provinciaRepository = new ProvinciaRepository(dataSource);
        
        provinciaRepository.findById(1L).ifPresent(p -> {
            provinciaRepository.delete(p);
        });      
        
        Optional<ProvinciaEntity> optional = provinciaRepository.findById(1L);
        assertThat(optional).isNotPresent();        
    }
}
