package com.parking.service.interfaces;

import com.parking.model.Floor;
import com.parking.model.Slot;
import com.parking.model.VehicleType;
import java.util.List;

/**
 * Interface for slot allocation strategies.
 * Dependency Inversion: Services depend on this abstraction, not concrete implementations.
 */
public interface SlotAllocationStrategy {
    
    /**
     * Find an available slot for a given vehicle type.
     * @param floors List of floors to search
     * @param vehicleType Type of vehicle to park
     * @return An available slot, or null if none found
     */
    Slot findAvailableSlot(List<Floor> floors, VehicleType vehicleType);
}
