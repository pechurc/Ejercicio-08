package com.eiv.dtos;

public class LocalidadDtoImpl implements LocalidadDto {
    
    private Long id;
    private String nombre;
    private Long provinciaId;
    
    public LocalidadDtoImpl(String nombre, Long provinciaId) {
        super();
        this.nombre = nombre;
        this.provinciaId = provinciaId;
    }
    
    public LocalidadDtoImpl(Long id, String nombre, Long provinciaId) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.provinciaId = provinciaId;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public Long getProvinciaId() {
        return this.provinciaId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProvinciaId(Long provinciaId) {
        this.provinciaId = provinciaId;
    }

}
