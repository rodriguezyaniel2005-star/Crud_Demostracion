package org.example.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central registry and dispatcher for entity change events - implements singleton Observer pattern
 */
public class EntityChangeManager {
    private static EntityChangeManager instance;
    private final Map<String, List<EntityChangeListener>> listenersByEntityType;
    private final List<EntityChangeListener> globalListeners;
    
    private EntityChangeManager() {
        this.listenersByEntityType = new HashMap<>();
        this.globalListeners = new CopyOnWriteArrayList<>();
        
        // Initialize listener lists for each entity type
        listenersByEntityType.put(EntityChangeEvent.ENTITY_CATEGORIA, new CopyOnWriteArrayList<>());
        listenersByEntityType.put(EntityChangeEvent.ENTITY_BUYER, new CopyOnWriteArrayList<>());
        listenersByEntityType.put(EntityChangeEvent.ENTITY_DISCO, new CopyOnWriteArrayList<>());
        listenersByEntityType.put(EntityChangeEvent.ENTITY_SALE, new CopyOnWriteArrayList<>());
    }
    
    /**
     * Get singleton instance of EntityChangeManager
     */
    public static synchronized EntityChangeManager getInstance() {
        if (instance == null) {
            instance = new EntityChangeManager();
        }
        return instance;
    }
    
    /**
     * Register a listener for a specific entity type
     */
    public void addListener(String entityType, EntityChangeListener listener) {
        List<EntityChangeListener> listeners = listenersByEntityType.get(entityType);
        if (listeners != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Register a listener for all entity types
     */
    public void addGlobalListener(EntityChangeListener listener) {
        if (!globalListeners.contains(listener)) {
            globalListeners.add(listener);
        }
    }
    
    /**
     * Unregister a listener for a specific entity type
     */
    public void removeListener(String entityType, EntityChangeListener listener) {
        List<EntityChangeListener> listeners = listenersByEntityType.get(entityType);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Unregister a listener from all entity types and global list
     */
    public void removeListener(EntityChangeListener listener) {
        listenersByEntityType.values().forEach(listeners -> listeners.remove(listener));
        globalListeners.remove(listener);
    }
    
    /**
     * Publish an entity change event to all interested listeners
     */
    public void publishEvent(EntityChangeEvent event) {
        // Notify entity-type-specific listeners
        List<EntityChangeListener> listeners = listenersByEntityType.get(event.getEntityType());
        if (listeners != null) {
            for (EntityChangeListener listener : listeners) {
                listener.onEntityChanged(event);
            }
        }
        
        // Notify global listeners
        for (EntityChangeListener listener : globalListeners) {
            listener.onEntityChanged(event);
        }
    }
    
    /**
     * Clear all listeners (useful for testing)
     */
    public void clearAllListeners() {
        listenersByEntityType.values().forEach(List::clear);
        globalListeners.clear();
    }
}
