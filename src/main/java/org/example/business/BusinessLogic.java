package org.example.business;

import java.util.List;

import org.bson.types.ObjectId;
import org.example.data.BuyerDAO;
import org.example.data.CategoriaDAO;
import org.example.data.DiscoDAO;
import org.example.data.SaleDAO;
import org.example.event.EntityChangeEvent;
import org.example.event.EntityChangeManager;
import org.example.model.Buyer;
import org.example.model.Categoria;
import org.example.model.Disco;
import org.example.model.Sale;

/**
 * BusinessLogic: Core application logic layer
 * Handles CRUD operations and event publishing for all entities
 */
public class BusinessLogic {
    private DiscoDAO discoDAO;  // Named DiscoDAO but represents Producto (for backward compatibility)
    private CategoriaDAO categoriaDAO;
    private BuyerDAO buyerDAO;
    private SaleDAO saleDAO;
    private EntityChangeManager eventManager;
    
    public BusinessLogic() {
        this.discoDAO = new DiscoDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.buyerDAO = new BuyerDAO();
        this.saleDAO = new SaleDAO();
        this.eventManager = EntityChangeManager.getInstance();
    }
    
    // ============ PRODUCTO OPERATIONS (Disco model class, Producto logic) ============
    
    public boolean createProducto(Disco producto) {
        try {
            if (Validator.isValidProductData(producto.getNombre(), producto.getDescripcion(), 
                    producto.getMarca(), producto.getPrecio(), producto.getStock())) {
                discoDAO.create(producto);
                eventManager.publishEvent(new EntityChangeEvent(
                    EntityChangeEvent.ENTITY_PRODUCTO, EntityChangeEvent.OPERATION_CREATE, producto.getId(), producto));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Disco getProducto(ObjectId id) {
        try {
            return discoDAO.read(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Disco> getAllProductos() {
        try {
            return discoDAO.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateProducto(Disco producto) {
        try {
            if (Validator.isValidProductData(producto.getNombre(), producto.getDescripcion(), 
                    producto.getMarca(), producto.getPrecio(), producto.getStock())) {
                discoDAO.update(producto);
                eventManager.publishEvent(new EntityChangeEvent(
                    EntityChangeEvent.ENTITY_PRODUCTO, EntityChangeEvent.OPERATION_UPDATE, producto.getId(), producto));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteProducto(ObjectId id) {
        try {
            discoDAO.delete(id);
            eventManager.publishEvent(new EntityChangeEvent(
                EntityChangeEvent.ENTITY_PRODUCTO, EntityChangeEvent.OPERATION_DELETE, id, null));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ CATEGORIA OPERATIONS ============
    
    public boolean createCategoria(Categoria categoria) {
        try {
            if (Validator.isValidCategoriaData(categoria.getNombre(), categoria.getDescripcion())) {
                categoriaDAO.create(categoria);
                eventManager.publishEvent(new EntityChangeEvent(
                    EntityChangeEvent.ENTITY_CATEGORIA, EntityChangeEvent.OPERATION_CREATE, categoria.getId(), categoria));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Categoria getCategoria(ObjectId id) {
        try {
            return categoriaDAO.read(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Categoria> getAllCategorias() {
        try {
            return categoriaDAO.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateCategoria(Categoria categoria) {
        try {
            if (Validator.isValidCategoriaData(categoria.getNombre(), categoria.getDescripcion())) {
                categoriaDAO.update(categoria);
                eventManager.publishEvent(new EntityChangeEvent(
                    EntityChangeEvent.ENTITY_CATEGORIA, EntityChangeEvent.OPERATION_UPDATE, categoria.getId(), categoria));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCategoria(ObjectId id) {
        try {
            categoriaDAO.delete(id);
            eventManager.publishEvent(new EntityChangeEvent(
                EntityChangeEvent.ENTITY_CATEGORIA, EntityChangeEvent.OPERATION_DELETE, id, null));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ COMPRADOR OPERATIONS (Buyer model) ============
    
    public boolean createComprador(Buyer comprador) {
        try {
            if (Validator.isValidBuyerData(comprador.getNombre(), comprador.getApellidos(), 
                    comprador.getEmail(), comprador.getTelefono(), comprador.getDireccion())) {
                buyerDAO.create(comprador);
                eventManager.publishEvent(new EntityChangeEvent(
                    EntityChangeEvent.ENTITY_COMPRADOR, EntityChangeEvent.OPERATION_CREATE, comprador.getId(), comprador));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Buyer getComprador(ObjectId id) {
        try {
            return buyerDAO.read(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Buyer> getAllCompradores() {
        try {
            return buyerDAO.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateComprador(Buyer comprador) {
        try {
            if (Validator.isValidBuyerData(comprador.getNombre(), comprador.getApellidos(), 
                    comprador.getEmail(), comprador.getTelefono(), comprador.getDireccion())) {
                buyerDAO.update(comprador);
                eventManager.publishEvent(new EntityChangeEvent(
                    EntityChangeEvent.ENTITY_COMPRADOR, EntityChangeEvent.OPERATION_UPDATE, comprador.getId(), comprador));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteComprador(ObjectId id) {
        try {
            buyerDAO.delete(id);
            eventManager.publishEvent(new EntityChangeEvent(
                EntityChangeEvent.ENTITY_COMPRADOR, EntityChangeEvent.OPERATION_DELETE, id, null));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ VENTA OPERATIONS (Sale model) ============
    
    public boolean createVenta(Sale venta) {
        try {
            if (Validator.isValidSaleData(venta.getProductosComprados(), venta.getTotalVenta())) {
                saleDAO.create(venta);
                eventManager.publishEvent(new EntityChangeEvent(
                    EntityChangeEvent.ENTITY_VENTA, EntityChangeEvent.OPERATION_CREATE, venta.getId(), venta));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Sale getVenta(ObjectId id) {
        try {
            return saleDAO.read(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Sale> getAllVentas() {
        try {
            return saleDAO.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Sale> getVentasByComprador(ObjectId compradorId) {
        try {
            return saleDAO.readByBuyerId(compradorId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateVenta(Sale venta) {
        try {
            saleDAO.update(venta);
            eventManager.publishEvent(new EntityChangeEvent(
                EntityChangeEvent.ENTITY_VENTA, EntityChangeEvent.OPERATION_UPDATE, venta.getId(), venta));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteVenta(ObjectId id) {
        try {
            saleDAO.delete(id);
            eventManager.publishEvent(new EntityChangeEvent(
                EntityChangeEvent.ENTITY_VENTA, EntityChangeEvent.OPERATION_DELETE, id, null));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ============ LEGACY COMPATIBILITY METHODS ============
    
    // These methods maintain backward compatibility with old Disco/Buyer/Sale terminology
    
    public boolean createDisco(Disco disco) { return createProducto(disco); }
    public Disco getDisco(ObjectId id) { return getProducto(id); }
    public List<Disco> getAllDiscos() { return getAllProductos(); }
    public boolean updateDisco(Disco disco) { return updateProducto(disco); }
    public boolean deleteDisco(ObjectId id) { return deleteProducto(id); }
    
    public boolean createBuyer(Buyer buyer) { return createComprador(buyer); }
    public Buyer getBuyer(ObjectId id) { return getComprador(id); }
    public List<Buyer> getAllBuyers() { return getAllCompradores(); }
    public boolean updateBuyer(Buyer buyer) { return updateComprador(buyer); }
    public boolean deleteBuyer(ObjectId id) { return deleteComprador(id); }
    
    public boolean createSale(Sale sale) { return createVenta(sale); }
    public Sale getSale(ObjectId id) { return getVenta(id); }
    public List<Sale> getAllSales() { return getAllVentas(); }
    public List<Sale> getSalesByBuyer(ObjectId buyerId) { return getVentasByComprador(buyerId); }
    public boolean updateSale(Sale sale) { return updateVenta(sale); }
    public boolean deleteSale(ObjectId id) { return deleteVenta(id); }
}
