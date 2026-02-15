package com.parking.service.interfaces;

import com.parking.model.Floor;
import java.util.Map;

/**
 * Interface for Display Board Service operations.
 * Dependency Inversion: Controllers depend on this abstraction for display functionality.
 */
public interface IDisplayBoardService {
    
    /**
     * Update display board for a specific floor.
     * @param floor The floor to display
     */
    void update(Floor floor);
    
    /**
     * Show availability for all floors.
     */
    void showAllFloorsAvailability();
    
    /**
     * Get availability summary as a map.
     * @return Map of floor number to availability summary string
     */
    Map<Integer, String> getAvailabilitySummary();
    
    /**
     * Show compact view of availability.
     */
    void showCompactView();
    
    /**
     * Show detailed slot map for a floor.
     * @param floorNumber The floor number to display
     */
    void showSlotMap(int floorNumber);
}
