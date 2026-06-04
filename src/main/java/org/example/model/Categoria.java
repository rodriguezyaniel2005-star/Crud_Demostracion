package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.bson.types.ObjectId;

/**
 * Categoria: Product category for the online store
 * Supports multiple product categories with flexible attributes
 */
public class Categoria {
    private ObjectId id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaAlta;
    
    public Categoria() {
        this.fechaAlta = LocalDateTime.now();
    }
    
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaAlta = LocalDateTime.now();
    }
    
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
    
    @Override
    public String toString() {
        return nombre;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria that = (Categoria) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
