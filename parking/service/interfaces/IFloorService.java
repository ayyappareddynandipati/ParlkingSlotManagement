package com.parking.service.interfaces;

import com.parking.model.Floor;
import com.parking.model.Slot;
import com.parking.model.VehicleType;
import java.util.List;

/**
 * Interface for Floor Service operations.
 * Handles floor-level operations and slot management.
 */
public interface IFloorService {
    
    /**
     * Get all floors.
     * @return List of all floors
     */
    List<Floor> getAllFloors();
    
    /**
     * Get a specific floor.
     * @param floorNumber The floor number
     * @return The floor, or null if not found
     */
    Floor getFloor(int floorNumber);
    
    /**
     * Get available slots on a floor for a vehicle type.
     * @param floorNumber The floor number
     * @param type The vehicle type
     * @return List of available slots
     */
    List<Slot> getAvailableSlots(int floorNumber, VehicleType type);
    
    /**
     * Count available slots on a floor.
     * @param floorNumber The floor number
     * @return Number of available slots
     */
    int countAvailableSlots(int floorNumber);
    
    /**
     * Count occupied slots on a floor.
     * @param floorNumber The floor number
     * @return Number of occupied slots
     */
    int countOccupiedSlots(int floorNumber);
    
    /**
     * Set floor operational status.
     * @param floorNumber The floor number
     * @param operational true if operational, false otherwise
     * @return true if status was updated
     */
    boolean setFloorOperational(int floorNumber, boolean operational);
    
    /**
     * Check if floor is operational.
     * @param floorNumber The floor number
     * @return true if operational
     */
    boolean isFloorOperational(int floorNumber);
    
    /**
     * Get floor capacity.
     * @param floorNumber The floor number
     * @return Total capacity of the floor
     */
    int getFloorCapacity(int floorNumber);
    
    /**
     * Get floor occupancy percentage.
     * @param floorNumber The floor number
     * @return Occupancy percentage (0-100)
     */
    double getFloorOccupancy(int floorNumber);
}
