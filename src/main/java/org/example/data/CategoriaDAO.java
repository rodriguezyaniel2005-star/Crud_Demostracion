package org.example.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Categoria;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class CategoriaDAO {
    private MongoCollection<Document> collection;
    
    public CategoriaDAO() {
        MongoDatabase db = MongoConnection.getInstance();
        this.collection = db.getCollection("categorias");
    }
    
    public void create(Categoria categoria) {
        Document doc = new Document();
        doc.append("nombre", categoria.getNombre())
                .append("descripcion", categoria.getDescripcion())
                .append("fechaAlta", categoria.getFechaAlta());
        
        collection.insertOne(doc);
        categoria.setId((ObjectId) doc.get("_id"));
    }
    
    public Categoria read(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return documentToCategoria(doc);
    }
    
    public List<Categoria> readAll() {
        List<Categoria> categorias = new ArrayList<>();
        for (Document doc : collection.find()) {
            categorias.add(documentToCategoria(doc));
        }
        return categorias;
    }
    
    public void update(Categoria categoria) {
        collection.updateOne(
            Filters.eq("_id", categoria.getId()),
            new Document("$set", new Document()
                .append("nombre", categoria.getNombre())
                .append("descripcion", categoria.getDescripcion())
                .append("fechaAlta", categoria.getFechaAlta()))
        );
    }
    
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
    
    private Categoria documentToCategoria(Document doc) {
        if (doc == null) return null;
        
        Categoria categoria = new Categoria();
        categoria.setId((ObjectId) doc.get("_id"));
        categoria.setNombre(doc.getString("nombre"));
        categoria.setDescripcion(doc.getString("descripcion"));
        
        if (doc.get("fechaAlta") != null) {
            categoria.setFechaAlta(ConversionUtils.dateToLocalDateTime(doc.getDate("fechaAlta")));
        }
        
        return categoria;
    }
}
