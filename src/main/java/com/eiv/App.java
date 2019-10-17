package com.eiv;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.eiv.dtos.LocalidadDtoImpl;
import com.eiv.dtos.ProvinciaDtoImpl;
import com.eiv.entities.LocalidadEntity;
import com.eiv.entities.ProvinciaEntity;
import com.eiv.services.LocalidadService;
import com.eiv.services.ProvinciaService;

@Configuration
public class App {
        
    public static final ApplicationContext CTX;
    @Autowired
    private DataSource dataSource;
    @Autowired 
    ProvinciaService provinciaService;
    @Autowired 
    LocalidadService localidadService;

    static {
        CTX = new ClassPathXmlApplicationContext("app-config.xml");
    }
    
    public static void main(String[] args) throws Exception {
        App app = CTX.getBean(App.class);
        app.run();     
    }
    
    public void run() throws Exception {        
        // schema init
        Resource initSchema = new ClassPathResource("import.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        
        System.out.println("Buscamos la provincia con ID=1");
        // Busca una provincia
        ProvinciaEntity provincia = provinciaService.findById(1L).orElseThrow(() -> 
                new RuntimeException("Provincia no encontrada"));
        
        System.out.println(provincia);
        
        // Recupera todas las provincias
        List<ProvinciaEntity> provincias = provinciaService.buscarTodas();
        
        System.out.println("\nMostramos las provincias");
        for (ProvinciaEntity provinciaEntity : provincias) {
            System.out.println(provinciaEntity);
        }
        
        System.out.println("\nInsertamos una provincia");
        // Crea una provincia
        ProvinciaDtoImpl nuevaProvincia = new ProvinciaDtoImpl("Mendoza");        
        provinciaService.nueva(nuevaProvincia);
        
        // Recupera todas las provincias
        provincias = provinciaService.buscarTodas();
        
        System.out.println("\nVolvemos a mostrar las provincias");
        for (ProvinciaEntity provinciaEntity : provincias) {
            System.out.println(provinciaEntity);
        }
        
        
        System.out.println("\nBuscamos la localidad con ID=1");
        // Busca una provincia
        LocalidadEntity localidad = localidadService.findById(1L).orElseThrow(() -> 
                new RuntimeException("Provincia no encontrada"));
        
        System.out.println(localidad);
        
        // Recupera todas las localidades 
        List<LocalidadEntity> localidades = localidadService.buscarTodas();
        
        System.out.println("\nMostramos las localidades");
        for (LocalidadEntity localidadEntity : localidades) {
            System.out.println(localidadEntity);
        }
        
        System.out.println("\nInsertamos una localidad");
        // Crea una localidad nueva 
        LocalidadDtoImpl nuevaLocalidad = new LocalidadDtoImpl("Cordoba", 3L);
        localidadService.nueva(nuevaLocalidad);
        
        // Recupera todas las localidades 
        localidades = localidadService.buscarTodas();
        
        System.out.println("\nVolvemos a mostrar las localidades");
        for (LocalidadEntity localidadEntity : localidades) {
            System.out.println(localidadEntity);
        }
    }
}
