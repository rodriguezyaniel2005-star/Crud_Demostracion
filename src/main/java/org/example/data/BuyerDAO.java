package org.example.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Buyer;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class BuyerDAO {
    private MongoCollection<Document> collection;
    
    public BuyerDAO() {
        MongoDatabase db = MongoConnection.getInstance();
        this.collection = db.getCollection("compradores");
    }
    
    public void create(Buyer buyer) {
        Document doc = new Document();
        doc.append("nombre", buyer.getNombre())
                .append("apellidos", buyer.getApellidos())
                .append("email", buyer.getEmail())
                .append("telefono", buyer.getTelefono())
                .append("direccion", buyer.getDireccion())
                .append("fechaRegistro", buyer.getFechaRegistro());
        
        collection.insertOne(doc);
        buyer.setId((ObjectId) doc.get("_id"));
    }
    
    public Buyer read(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return documentToBuyer(doc);
    }
    
    public List<Buyer> readAll() {
        List<Buyer> buyers = new ArrayList<>();
        for (Document doc : collection.find()) {
            buyers.add(documentToBuyer(doc));
        }
        return buyers;
    }
    
    public void update(Buyer buyer) {
        collection.updateOne(
            Filters.eq("_id", buyer.getId()),
            new Document("$set", new Document()
                .append("nombre", buyer.getNombre())
                .append("apellidos", buyer.getApellidos())
                .append("email", buyer.getEmail())
                .append("telefono", buyer.getTelefono())
                .append("direccion", buyer.getDireccion())
                .append("fechaRegistro", buyer.getFechaRegistro()))
        );
    }
    
    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
    
    private Buyer documentToBuyer(Document doc) {
        if (doc == null) return null;
        
        Buyer buyer = new Buyer();
        buyer.setId((ObjectId) doc.get("_id"));
        buyer.setNombre(doc.getString("nombre"));
        buyer.setApellidos(doc.getString("apellidos"));
        buyer.setEmail(doc.getString("email"));
        buyer.setTelefono(doc.getString("telefono"));
        buyer.setDireccion(doc.getString("direccion"));
        
        if (doc.get("fechaRegistro") != null) {
            buyer.setFechaRegistro(ConversionUtils.dateToLocalDateTime(doc.getDate("fechaRegistro")));
        }
        
        return buyer;
    }
}
