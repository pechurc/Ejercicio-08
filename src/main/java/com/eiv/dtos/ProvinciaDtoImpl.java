package com.eiv.dtos;

public class ProvinciaDtoImpl implements ProvinciaDto {
    
    private String nombre;
    private Long id;
    
    public ProvinciaDtoImpl(String nombre) {
        this.nombre = nombre;
    }
    
    public ProvinciaDtoImpl(Long id, String nombre) {
        this.nombre = nombre;
        this.id = id;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

}
