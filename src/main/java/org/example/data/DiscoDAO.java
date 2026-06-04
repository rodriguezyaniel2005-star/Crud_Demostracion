package org.example.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Disco;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class DiscoDAO {
    private MongoCollection<Document> collection;
    
    public DiscoDAO() {
        MongoDatabase db = MongoConnection.getInstance();
        this.collection = db.getCollection("productos");
    }
    
    public void create(Disco disco) {
        Document doc = new Document();
        doc.append("nombre", disco.getNombre())
                .append("descripcion", disco.getDescripcion())
                .append("categoriaId", disco.getCategoriaId())
                .append("categoriaNombre", disco.getCategoriaNombre())
                .append("precio", disco.getPrecio())
                .append("stock", disco.getStock())
                .append("marca", disco.getMarca())
                .append("fechaAlta", disco.getFechaAlta());
        
        collection.insertOne(doc);
        disco.setId((ObjectId) doc.get("_id"));
    }
    
    public Disco read(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return documentToDisco(doc);
    }
    
    public List<Disco> readAll() {
        List<Disco> discos = new ArrayList<>();
        for (Document doc : collection.find()) {
            discos.add(documentToDisco(doc));
        }
        return discos;
    }
    
    public void update(Disco disco) {
        collection.updateOne(
            Filters.eq("_id", disco.getId()),
            new Document("$set", new Document()
                .append("nombre", disco.getNombre())
                .append("descripcion", disco.getDescripcion())
                .append("categoriaId", disco.getCategoriaId())
                .append("categoriaNombre", disco.getCategoriaNombre())
                .append("precio", disco.getPrecio())
                .append("stock", disco.getStock())
                .append("marca", disco.getMarca())
                .append("fechaAlta", disco.getFechaAlta()))
        );
    }
    
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
    
    private Disco documentToDisco(Document doc) {
        if (doc == null) return null;
        
        Disco disco = new Disco();
        disco.setId((ObjectId) doc.get("_id"));
        disco.setNombre(doc.getString("nombre"));
        disco.setDescripcion(doc.getString("descripcion"));
        disco.setCategoriaId((ObjectId) doc.get("categoriaId"));
        disco.setCategoriaNombre(doc.getString("categoriaNombre"));
        disco.setPrecio(doc.getDouble("precio"));
        disco.setStock(doc.getInteger("stock"));
        disco.setMarca(doc.getString("marca"));
        
        if (doc.get("fechaAlta") != null) {
            disco.setFechaAlta(ConversionUtils.dateToLocalDateTime(doc.getDate("fechaAlta")));
        }
        
        return disco;
    }
}
