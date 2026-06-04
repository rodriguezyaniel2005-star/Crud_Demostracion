package org.example.event;

/**
 * Interface for components that need to react to entity changes
 */
public interface EntityChangeListener {
    /**
     * Called when an entity changes (create, update, or delete)
     * @param event The entity change event containing type, operation, and entity data
     */
    void onEntityChanged(EntityChangeEvent event);
}
