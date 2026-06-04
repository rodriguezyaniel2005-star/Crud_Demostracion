package org.example.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;

/**
 * Sale (Venta): Represents a transaction in the online store
 * Can contain multiple products from different categories
 */
public class Sale {
    private ObjectId id;
    private ObjectId compradorId;
    private String compradorNombre;
    private List<ProductoVendido> productosComprados;
    private int cantidadProductos;
    private double subtotal;
    private double impuesto;
    private double totalVenta;
    private LocalDateTime fecha;
    private String estado;  // "Pendiente", "Completada", "Cancelada"
    
    /**
     * Nested class representing a product within a sale
     */
    public static class ProductoVendido {
        private ObjectId productoId;
        private String productoNombre;
        private String categoriaNombre;
        private int cantidad;
        private double precioUnitario;
        private double subtotal;
        
        public ProductoVendido() {}
        
        public ProductoVendido(ObjectId productoId, String productoNombre, String categoriaNombre, 
                              int cantidad, double precioUnitario) {
            this.productoId = productoId;
            this.productoNombre = productoNombre;
            this.categoriaNombre = categoriaNombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = cantidad * precioUnitario;
        }
        
        // Getters and Setters
        public ObjectId getProductoId() { return productoId; }
        public void setProductoId(ObjectId productoId) { this.productoId = productoId; }
        
        public String getProductoNombre() { return productoNombre; }
        public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
        
        public String getCategoriaNombre() { return categoriaNombre; }
        public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
        
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { 
            this.cantidad = cantidad;
            this.subtotal = cantidad * precioUnitario;
        }
        
        public double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(double precioUnitario) { 
            this.precioUnitario = precioUnitario;
            this.subtotal = cantidad * precioUnitario;
        }
        
        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
        
        @Override
        public String toString() {
            return productoNombre + " (" + cantidad + "x $" + precioUnitario + ")";
        }
    }
    
    public Sale() {
        this.fecha = LocalDateTime.now();
        this.estado = "Completada";
    }
    
    public Sale(ObjectId compradorId, String compradorNombre, List<ProductoVendido> productosComprados, 
               double subtotal, double impuesto, double totalVenta) {
        this.compradorId = compradorId;
        this.compradorNombre = compradorNombre;
        this.productosComprados = productosComprados;
        this.cantidadProductos = productosComprados.size();
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.totalVenta = totalVenta;
        this.fecha = LocalDateTime.now();
        this.estado = "Completada";
    }
    
    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public ObjectId getCompradorId() { return compradorId; }
    public void setCompradorId(ObjectId compradorId) { this.compradorId = compradorId; }
    
    public String getCompradorNombre() { return compradorNombre; }
    public void setCompradorNombre(String compradorNombre) { this.compradorNombre = compradorNombre; }
    
    public List<ProductoVendido> getProductosComprados() { return productosComprados; }
    public void setProductosComprados(List<ProductoVendido> productosComprados) { 
        this.productosComprados = productosComprados;
        this.cantidadProductos = productosComprados != null ? productosComprados.size() : 0;
    }
    
    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    
    public double getImpuesto() { return impuesto; }
    public void setImpuesto(double impuesto) { this.impuesto = impuesto; }
    
    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return compradorNombre + " - $" + totalVenta + " (" + cantidadProductos + " productos)";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
