package org.example.controller;

import org.example.model.Categoria;
import org.bson.types.ObjectId;
import java.util.List;

public class CategoriaController extends BaseController {
    
    public boolean crearCategoria(String nombre, String descripcion) {
        Categoria categoria = new Categoria(nombre, descripcion);
        return businessLogic.createCategoria(categoria);
    }
    
    public Categoria obtenerCategoria(ObjectId id) {
        return businessLogic.getCategoria(id);
    }
    
    public List<Categoria> obtenerTodasCategorias() {
        return businessLogic.getAllCategorias();
    }
    
    public boolean actualizarCategoria(ObjectId id, String nombre, String descripcion) {
        Categoria categoria = businessLogic.getCategoria(id);
        if (categoria != null) {
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            return businessLogic.updateCategoria(categoria);
        }
        return false;
    }
    
    public boolean eliminarCategoria(ObjectId id) {
        return businessLogic.deleteCategoria(id);
    }
}
