package org.example.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Sale;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * SaleDAO: Data access object for Sale (Venta) entities
 * Handles CRUD operations for sales with multiple products from different categories
 */
public class SaleDAO {
    private MongoCollection<Document> collection;
    
    public SaleDAO() {
        MongoDatabase db = MongoConnection.getInstance();
        this.collection = db.getCollection("ventas");
    }
    
    public void create(Sale sale) {
        List<Document> productoDocs = new ArrayList<>();
        if (sale.getProductosComprados() != null) {
            for (Sale.ProductoVendido producto : sale.getProductosComprados()) {
                Document productoDoc = new Document();
                productoDoc.append("productoId", producto.getProductoId())
                        .append("productoNombre", producto.getProductoNombre())
                        .append("categoriaNombre", producto.getCategoriaNombre())
                        .append("cantidad", producto.getCantidad())
                        .append("precioUnitario", producto.getPrecioUnitario())
                        .append("subtotal", producto.getSubtotal());
                productoDocs.add(productoDoc);
            }
        }
        
        Document doc = new Document();
        doc.append("compradorId", sale.getCompradorId())
                .append("compradorNombre", sale.getCompradorNombre())
                .append("productosComprados", productoDocs)
                .append("cantidadProductos", sale.getCantidadProductos())
                .append("subtotal", sale.getSubtotal())
                .append("impuesto", sale.getImpuesto())
                .append("totalVenta", sale.getTotalVenta())
                .append("fecha", ConversionUtils.localDateTimeToDate(sale.getFecha()))
                .append("estado", sale.getEstado());
        
        collection.insertOne(doc);
        sale.setId((ObjectId) doc.get("_id"));
    }
    
    public Sale read(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return documentToSale(doc);
    }
    
    public List<Sale> readAll() {
        List<Sale> sales = new ArrayList<>();
        for (Document doc : collection.find()) {
            sales.add(documentToSale(doc));
        }
        return sales;
    }
    
    public List<Sale> readByBuyerId(ObjectId buyerId) {
        List<Sale> sales = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("compradorId", buyerId))) {
            sales.add(documentToSale(doc));
        }
        return sales;
    }
    
    public void update(Sale sale) {
        List<Document> productoDocs = new ArrayList<>();
        if (sale.getProductosComprados() != null) {
            for (Sale.ProductoVendido producto : sale.getProductosComprados()) {
                Document productoDoc = new Document();
                productoDoc.append("productoId", producto.getProductoId())
                        .append("productoNombre", producto.getProductoNombre())
                        .append("categoriaNombre", producto.getCategoriaNombre())
                        .append("cantidad", producto.getCantidad())
                        .append("precioUnitario", producto.getPrecioUnitario())
                        .append("subtotal", producto.getSubtotal());
                productoDocs.add(productoDoc);
            }
        }
        
        collection.updateOne(
            Filters.eq("_id", sale.getId()),
            new Document("$set", new Document()
                .append("compradorId", sale.getCompradorId())
                .append("compradorNombre", sale.getCompradorNombre())
                .append("productosComprados", productoDocs)
                .append("cantidadProductos", sale.getCantidadProductos())
                .append("subtotal", sale.getSubtotal())
                .append("impuesto", sale.getImpuesto())
                .append("totalVenta", sale.getTotalVenta())
                .append("fecha", ConversionUtils.localDateTimeToDate(sale.getFecha()))
                .append("estado", sale.getEstado()))
        );
    }
    
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
    
    private Sale documentToSale(Document doc) {
        if (doc == null) return null;
        
        Sale sale = new Sale();
        sale.setId((ObjectId) doc.get("_id"));
        sale.setCompradorId((ObjectId) doc.get("compradorId"));
        sale.setCompradorNombre(doc.getString("compradorNombre"));
        sale.setCantidadProductos(doc.getInteger("cantidadProductos"));
        sale.setSubtotal(doc.getDouble("subtotal"));
        sale.setImpuesto(doc.getDouble("impuesto"));
        sale.setTotalVenta(doc.getDouble("totalVenta"));
        sale.setEstado(doc.getString("estado"));
        
        if (doc.get("fecha") != null) {
            sale.setFecha(ConversionUtils.dateToLocalDateTime(doc.getDate("fecha")));
        }
        
        List<Document> productoDocs = (List<Document>) doc.get("productosComprados");
        List<Sale.ProductoVendido> productos = new ArrayList<>();
        if (productoDocs != null) {
            for (Document productoDoc : productoDocs) {
                Sale.ProductoVendido producto = new Sale.ProductoVendido();
                producto.setProductoId((ObjectId) productoDoc.get("productoId"));
                producto.setProductoNombre(productoDoc.getString("productoNombre"));
                producto.setCategoriaNombre(productoDoc.getString("categoriaNombre"));
                producto.setCantidad(productoDoc.getInteger("cantidad"));
                producto.setPrecioUnitario(productoDoc.getDouble("precioUnitario"));
                producto.setSubtotal(productoDoc.getDouble("subtotal"));
                productos.add(producto);
            }
        }
        sale.setProductosComprados(productos);
        
        return sale;
    }
}
