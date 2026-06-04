package org.example.controller;

import org.example.model.Disco;
import org.bson.types.ObjectId;
import java.util.List;

public class ProductController extends BaseController {
    
    public boolean crearDisco(String nombre, String descripcion, ObjectId categoriaId, 
                              double precio, int stock, String marca) {
        Disco disco = new Disco(nombre, descripcion, categoriaId, precio, stock, marca);
        return businessLogic.createDisco(disco);
    }
    
    public Disco obtenerDisco(ObjectId id) {
        return businessLogic.getDisco(id);
    }
    
    public List<Disco> obtenerTodosDiscos() {
        return businessLogic.getAllDiscos();
    }
    
    public boolean actualizarDisco(ObjectId id, String nombre, String descripcion, 
                                   ObjectId categoriaId, double precio, int stock, String marca) {
        Disco disco = businessLogic.getDisco(id);
        if (disco != null) {
            disco.setNombre(nombre);
            disco.setDescripcion(descripcion);
            disco.setCategoriaId(categoriaId);
            disco.setPrecio(precio);
            disco.setStock(stock);
            disco.setMarca(marca);
            return businessLogic.updateDisco(disco);
        }
        return false;
    }
    
    public boolean eliminarDisco(ObjectId id) {
        return businessLogic.deleteDisco(id);
    }
}
