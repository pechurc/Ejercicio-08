package com.eiv.entities;

public class LocalidadEntity {
    private Long id;
    private String nombre;
    private Long provinciaId;
    
    public LocalidadEntity() {
        
    }
    
    public LocalidadEntity(Long id, String nombre, Long provinciaId) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.provinciaId = provinciaId;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }    

    public Long getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Long provinciaId) {
        this.provinciaId = provinciaId;
    }
    
    @Override
    public String toString() {
        return "LocalidadEntity [id=" + id + ", nombre=" + nombre + ", provinciaId=" + provinciaId 
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LocalidadEntity other = (LocalidadEntity) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (nombre == null) {
            if (other.nombre != null) {
                return false;
            }
        } else if (!nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }
}
