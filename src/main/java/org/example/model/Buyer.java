package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.bson.types.ObjectId;

/**
 * Comprador: Represents an online store customer
 */
public class Buyer {
    private ObjectId id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaRegistro;
    
    public Buyer() {
        this.fechaRegistro = LocalDateTime.now();
    }
    
    public Buyer(String nombre, String apellidos, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaRegistro = LocalDateTime.now();
    }
    
    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Buyer buyer = (Buyer) o;
        return Objects.equals(id, buyer.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
