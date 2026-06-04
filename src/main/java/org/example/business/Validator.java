package org.example.business;

/**
 * Validator: Contains validation logic for all entity fields
 */
public class Validator {
    
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    public static boolean isValidEmail(String email) {
        if (!isValidString(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public static boolean isValidPhone(String phone) {
        if (!isValidString(phone)) return false;
        return phone.matches("^[0-9\\-\\+\\s()]+$");
    }
    
    public static boolean isValidPrice(double price) {
        return price > 0;
    }
    
    public static boolean isValidStock(int stock) {
        return stock >= 0;
    }
    
    /**
     * Validate product (Disco) data for the new product-based schema
     * nombre: product name
     * descripcion: product description
     * marca: brand/manufacturer
     * precio: price per unit
     * stock: inventory quantity
     */
    public static boolean isValidProductData(String nombre, String descripcion, String marca, double precio, int stock) {
        return isValidString(nombre) && 
               isValidString(descripcion) && 
               isValidString(marca) && 
               isValidPrice(precio) && 
               isValidStock(stock);
    }
    
    /**
     * Legacy method for compatibility - validates product data
     */
    public static boolean isValidDiscoData(String nombre, String descripcion, String marca, double precio, int stock) {
        return isValidProductData(nombre, descripcion, marca, precio, stock);
    }
    
    public static boolean isValidCategoriaData(String nombre, String descripcion) {
        return isValidString(nombre) && isValidString(descripcion);
    }
    
    /**
     * Validate buyer data - now includes apellidos (last name)
     */
    public static boolean isValidBuyerData(String nombre, String apellidos, String email, String telefono, String direccion) {
        return isValidString(nombre) && 
               isValidString(apellidos) &&
               isValidEmail(email) && 
               isValidPhone(telefono) && 
               isValidString(direccion);
    }
    
    /**
     * Legacy method for old Buyer validation without apellidos
     */
    public static boolean isValidBuyerDataLegacy(String nombre, String email, String telefono, String direccion) {
        return isValidString(nombre) && 
               isValidEmail(email) && 
               isValidPhone(telefono) && 
               isValidString(direccion);
    }
    
    /**
     * Validate sale data
     */
    public static boolean isValidSaleData(java.util.List<?> productosComprados, double totalVenta) {
        return productosComprados != null && !productosComprados.isEmpty() && totalVenta > 0;
    }
}
