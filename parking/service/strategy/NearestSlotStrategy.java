package com.parking.service.strategy;

import com.parking.model.Floor;
import com.parking.model.Slot;
import com.parking.model.VehicleType;
import com.parking.service.interfaces.SlotAllocationStrategy;
import java.util.List;

/**
 * First available slot allocation strategy.
 * Allocates the first available slot found (nearest to entry).
 */
public class NearestSlotStrategy implements SlotAllocationStrategy {
    
    @Override
    public Slot findAvailableSlot(List<Floor> floors, VehicleType vehicleType) {
        // Start from first floor (nearest to entry)
        for (Floor floor : floors) {
            if (!floor.isOperational()) {
                continue;
            }
            
            Slot slot = floor.findAvailableSlot(vehicleType);
            if (slot != null) {
                return slot;
            }
        }
        return null;
    }
}
