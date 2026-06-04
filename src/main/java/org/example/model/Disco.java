package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.bson.types.ObjectId;

/**
 * Producto: Represents a product in the online store
 * Supports flexible attributes based on product category
 * MongoDB allows each product to have category-specific attributes
 */
public class Disco {
    private ObjectId id;
    private String nombre;
    private String descripcion;
    private ObjectId categoriaId;
    private String categoriaNombre;
    private double precio;
    private int stock;
    private String marca;
    private LocalDateTime fechaAlta;
    
    // These will be populated based on category from MongoDB
    private String atributosEspecificos;
    
    public Disco() {
        this.fechaAlta = LocalDateTime.now();
    }
    
    public Disco(String nombre, String descripcion, ObjectId categoriaId, double precio, int stock, String marca) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.precio = precio;
        this.stock = stock;
        this.marca = marca;
        this.fechaAlta = LocalDateTime.now();
    }
    
    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public ObjectId getCategoriaId() { return categoriaId; }
    public void setCategoriaId(ObjectId categoriaId) { this.categoriaId = categoriaId; }
    
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
    
    public String getAtributosEspecificos() { return atributosEspecificos; }
    public void setAtributosEspecificos(String atributosEspecificos) { this.atributosEspecificos = atributosEspecificos; }
    
    @Override
    public String toString() {
        return nombre + " (" + marca + ") - $" + precio;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disco disco = (Disco) o;
        return Objects.equals(id, disco.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
