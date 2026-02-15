package com.parking.service.interfaces;

/**
 * Base interface for Gate Handler operations.
 * Provides common functionality for all gate types.
 */
public interface IGateHandler {
    
    /**
     * Get the gate ID.
     * @return The unique gate identifier
     */
    String getGateId();
}
