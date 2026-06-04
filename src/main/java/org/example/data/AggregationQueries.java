package org.example.data;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.*;

/**
 * AggregationQueries: Advanced MongoDB Aggregation Framework queries
 * Demonstrates complex data analysis capabilities of MongoDB
 */
public class AggregationQueries {
    
    private MongoCollection<Document> ventasCollection;
    private MongoCollection<Document> compradorCollection;
    private MongoCollection<Document> productoCollection;
    
    public AggregationQueries() {
        MongoDatabase db = MongoConnection.getInstance();
        this.ventasCollection = db.getCollection("ventas");
        this.compradorCollection = db.getCollection("compradores");
        this.productoCollection = db.getCollection("productos");
    }
    
    /**
     * Complex aggregation query showing complete buyer analysis
     * Returns buyer details with sales statistics, products purchased, and metrics
     * 
     * Uses:
     * - $lookup: Join ventas with compradores
     * - $group: Aggregate sales data
     * - $project: Shape output
     * - $unwind: Deconstruct arrays
     * - $sort: Sort by total spent
     */
    public List<Document> getBuyerSalesAnalysis() {
        List<Document> pipeline = Arrays.asList(
            // Stage 1: Match all sales (optional, can filter by date or buyer)
            new Document("$match", new Document()),
            
            // Stage 2: Lookup buyer details
            new Document("$lookup", new Document()
                .append("from", "compradores")
                .append("localField", "compradorId")
                .append("foreignField", "_id")
                .append("as", "comprador")),
            
            // Stage 3: Unwind buyer array (from lookup)
            new Document("$unwind", new Document("path", "$comprador")),
            
            // Stage 4: Unwind products array to process each product
            new Document("$unwind", new Document("path", "$productosComprados")),
            
            // Stage 5: Lookup product details for each product
            new Document("$lookup", new Document()
                .append("from", "productos")
                .append("localField", "productosComprados.productoId")
                .append("foreignField", "_id")
                .append("as", "detalleProducto")),
            
            // Stage 6: Unwind product details
            new Document("$unwind", new Document("path", "$detalleProducto")
                .append("preserveNullAndEmptyArrays", true)),
            
            // Stage 7: Group by buyer to aggregate statistics
            new Document("$group", new Document()
                .append("_id", "$compradorId")
                .append("nombre", new Document("$first", "$comprador.nombre"))
                .append("apellidos", new Document("$first", "$comprador.apellidos"))
                .append("email", new Document("$first", "$comprador.email"))
                .append("telefono", new Document("$first", "$comprador.telefono"))
                .append("direccion", new Document("$first", "$comprador.direccion"))
                .append("fechaRegistro", new Document("$first", "$comprador.fechaRegistro"))
                .append("totalCompras", new Document("$sum", 1))
                .append("totalGastado", new Document("$sum", "$totalVenta"))
                .append("totalItems", new Document("$sum", "$cantidadProductos"))
                .append("categoriasDiferentes", new Document("$addToSet", "$productosComprados.categoriaNombre"))
                .append("productosCaro", new Document("$max", "$productosComprados.precioUnitario"))
                .append("productoMasBarato", new Document("$min", "$productosComprados.precioUnitario"))
                .append("ultimaCompra", new Document("$max", "$fecha"))),
            
            // Stage 8: Sort by total spent descending
            new Document("$sort", new Document("totalGastado", -1))
        );
        
        List<Document> results = new ArrayList<>();
        AggregateIterable<Document> aggregation = ventasCollection.aggregate(pipeline);
        for (Document doc : aggregation) {
            results.add(doc);
        }
        return results;
    }
    
    /**
     * Get sales by category - shows which categories are most popular
     * Uses: $unwind, $group, $sort, $lookup
     */
    public List<Document> getSalesByCategory() {
        List<Document> pipeline = Arrays.asList(
            // Unwind products array
            new Document("$unwind", new Document("path", "$productosComprados")),
            
            // Group by category
            new Document("$group", new Document()
                .append("_id", "$productosComprados.categoriaNombre")
                .append("totalVentas", new Document("$sum", 1))
                .append("totalIngresos", new Document("$sum", "$productosComprados.subtotal"))
                .append("cantidadProductosVendidos", new Document("$sum", "$productosComprados.cantidad"))),
            
            // Sort by total ingresos
            new Document("$sort", new Document("totalIngresos", -1))
        );
        
        List<Document> results = new ArrayList<>();
        AggregateIterable<Document> aggregation = ventasCollection.aggregate(pipeline);
        for (Document doc : aggregation) {
            results.add(doc);
        }
        return results;
    }
    
    /**
     * Get top products - shows most sold and most profitable products
     * Uses: $unwind, $group, $sort, $limit
     */
    public List<Document> getTopProducts(int limit) {
        List<Document> pipeline = Arrays.asList(
            // Unwind products
            new Document("$unwind", new Document("path", "$productosComprados")),
            
            // Group by product
            new Document("$group", new Document()
                .append("_id", "$productosComprados.productoNombre")
                .append("categoria", new Document("$first", "$productosComprados.categoriaNombre"))
                .append("cantidadVendida", new Document("$sum", "$productosComprados.cantidad"))
                .append("ingresoTotal", new Document("$sum", "$productosComprados.subtotal"))
                .append("precioPromedio", new Document("$avg", "$productosComprados.precioUnitario"))),
            
            // Sort by cantidad vendida
            new Document("$sort", new Document("cantidadVendida", -1)),
            
            // Limit results
            new Document("$limit", limit)
        );
        
        List<Document> results = new ArrayList<>();
        AggregateIterable<Document> aggregation = ventasCollection.aggregate(pipeline);
        for (Document doc : aggregation) {
            results.add(doc);
        }
        return results;
    }
    
    /**
     * Get buyer spending ranges - categorize buyers by spending level
     * Uses: $group, $match (in stages)
     */
    public List<Document> getBuyerSpendingRanges() {
        List<Document> pipeline = Arrays.asList(
            // Group by buyer with total spent
            new Document("$group", new Document()
                .append("_id", new Document()
                    .append("compradorId", "$compradorId")
                    .append("compradorNombre", "$compradorNombre"))
                .append("totalGastado", new Document("$sum", "$totalVenta"))
                .append("cantidadCompras", new Document("$sum", 1))),
            
            // Project with spending category
            new Document("$project", new Document()
                .append("compradorId", "$_id.compradorId")
                .append("compradorNombre", "$_id.compradorNombre")
                .append("totalGastado", 1)
                .append("cantidadCompras", 1)
                .append("categoriaGasto", new Document("$cond", new Object[]{
                    new Document("$gte", Arrays.asList("$totalGastado", 5000)),
                    "Premium",
                    new Document("$cond", new Object[]{
                        new Document("$gte", Arrays.asList("$totalGastado", 2000)),
                        "VIP",
                        new Document("$cond", new Object[]{
                            new Document("$gte", Arrays.asList("$totalGastado", 500)),
                            "Regular",
                            "Ocasional"
                        })
                    })
                }))
                .append("_id", 0)),
            
            // Sort by spending
            new Document("$sort", new Document("totalGastado", -1))
        );
        
        List<Document> results = new ArrayList<>();
        AggregateIterable<Document> aggregation = ventasCollection.aggregate(pipeline);
        for (Document doc : aggregation) {
            results.add(doc);
        }
        return results;
    }
    
    /**
     * Sales summary - overall statistics
     * Uses: $group with various accumulators
     */
    public Document getSalesSummary() {
        List<Document> pipeline = Arrays.asList(
            // Group all documents
            new Document("$group", new Document()
                .append("_id", null)
                .append("totalVentas", new Document("$sum", 1))
                .append("totalIngresos", new Document("$sum", "$totalVenta"))
                .append("ventaPromedio", new Document("$avg", "$totalVenta"))
                .append("ventaMaxima", new Document("$max", "$totalVenta"))
                .append("ventaMinima", new Document("$min", "$totalVenta"))
                .append("compradores", new Document("$addToSet", "$compradorNombre"))
                .append("cantidadCompradores", new Document("$sum", 1))),
            
            // Project to rename fields
            new Document("$project", new Document()
                .append("_id", 0)
                .append("totalVentas", 1)
                .append("totalIngresos", 1)
                .append("ventaPromedio", new Document("$round", Arrays.asList("$ventaPromedio", 2)))
                .append("ventaMaxima", 1)
                .append("ventaMinima", 1)
                .append("cantidadCompradores", 1))
        );
        
        AggregateIterable<Document> aggregation = ventasCollection.aggregate(pipeline);
        return aggregation.first();
    }
    
    /**
     * Print a document in a readable format
     */
    public static void printDocument(Document doc) {
        if (doc == null) {
            System.out.println("No results");
            return;
        }
        System.out.println(doc.toJson());
    }
    
    /**
     * Print a list of documents
     */
    public static void printDocuments(List<Document> docs) {
        if (docs == null || docs.isEmpty()) {
            System.out.println("No results");
            return;
        }
        for (int i = 0; i < docs.size(); i++) {
            System.out.println("\n=== Result " + (i + 1) + " ===");
            System.out.println(docs.get(i).toJson());
        }
    }
}
