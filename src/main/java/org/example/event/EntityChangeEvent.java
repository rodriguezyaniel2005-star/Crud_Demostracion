package org.example.event;

import org.bson.types.ObjectId;

/**
 * Represents a change event for any entity (Categoria, Buyer, Producto, Sale)
 */
public class EntityChangeEvent {
    // Entity type constants
    public static final String ENTITY_CATEGORIA = "CATEGORIA";
    public static final String ENTITY_BUYER = "BUYER";
    public static final String ENTITY_COMPRADOR = "COMPRADOR";  // Alias for BUYER
    public static final String ENTITY_PRODUCTO = "PRODUCTO";
    public static final String ENTITY_DISCO = "DISCO";  // Legacy compatibility
    public static final String ENTITY_SALE = "SALE";
    public static final String ENTITY_VENTA = "VENTA";  // Alias for SALE
    
    // Operation type constants
    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_DELETE = "DELETE";
    
    private final String entityType;
    private final String operation;
    private final ObjectId entityId;
    private final Object entityData; // The actual entity object (Categoria, Buyer, Producto, etc.)
    
    public EntityChangeEvent(String entityType, String operation, ObjectId entityId, Object entityData) {
        this.entityType = entityType;
        this.operation = operation;
        this.entityId = entityId;
        this.entityData = entityData;
    }
    
    public String getEntityType() { return entityType; }
    public String getOperation() { return operation; }
    public ObjectId getEntityId() { return entityId; }
    public Object getEntityData() { return entityData; }
    
    @Override
    public String toString() {
        return String.format("EntityChangeEvent[type=%s, operation=%s, id=%s]", 
            entityType, operation, entityId);
    }
}
