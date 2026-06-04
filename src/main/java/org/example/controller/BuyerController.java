package org.example.controller;

import org.example.model.Buyer;
import org.bson.types.ObjectId;
import java.util.List;

public class BuyerController extends BaseController {
    
    public boolean crearComprador(String nombre, String apellidos, String email, String telefono, String direccion) {
        Buyer buyer = new Buyer(nombre, apellidos, email, telefono, direccion);
        return businessLogic.createBuyer(buyer);
    }
    
    public Buyer obtenerComprador(ObjectId id) {
        return businessLogic.getBuyer(id);
    }
    
    public List<Buyer> obtenerTodosCompradores() {
        return businessLogic.getAllBuyers();
    }
    
    public boolean actualizarComprador(ObjectId id, String nombre, String apellidos, String email, 
                                       String telefono, String direccion) {
        Buyer buyer = businessLogic.getBuyer(id);
        if (buyer != null) {
            buyer.setNombre(nombre);
            buyer.setApellidos(apellidos);
            buyer.setEmail(email);
            buyer.setTelefono(telefono);
            buyer.setDireccion(direccion);
            return businessLogic.updateBuyer(buyer);
        }
        return false;
    }
    
    public boolean eliminarComprador(ObjectId id) {
        return businessLogic.deleteBuyer(id);
    }
}
