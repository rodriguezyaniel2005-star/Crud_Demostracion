package org.example.controller;

import org.example.model.Sale;
import org.bson.types.ObjectId;
import java.util.List;

public class SaleController extends BaseController {
    
    public boolean crearVenta(Sale sale) {
        return businessLogic.createSale(sale);
    }
    
    public Sale obtenerVenta(ObjectId id) {
        return businessLogic.getSale(id);
    }
    
    public List<Sale> obtenerTodasVentas() {
        return businessLogic.getAllSales();
    }
    
    public List<Sale> obtenerVentasPorComprador(ObjectId buyerId) {
        return businessLogic.getSalesByBuyer(buyerId);
    }
    
    public boolean actualizarVenta(Sale sale) {
        return businessLogic.updateSale(sale);
    }
    
    public boolean eliminarVenta(ObjectId id) {
        return businessLogic.deleteSale(id);
    }
}
