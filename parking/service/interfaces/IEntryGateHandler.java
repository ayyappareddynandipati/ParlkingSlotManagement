package com.parking.service.interfaces;

import com.parking.model.Ticket;
import com.parking.model.Vehicle;
import com.parking.model.VehicleType;

/**
 * Interface for Entry Gate Handler operations.
 * Dependency Inversion: Controllers depend on this abstraction for entry operations.
 */
public interface IEntryGateHandler extends IGateHandler {
    
    /**
     * Process vehicle entry through this gate.
     * @param vehicle The vehicle entering
     * @return The parking ticket, or null if entry failed
     */
    Ticket processEntry(Vehicle vehicle);
    
    /**
     * Display current availability at this gate.
     */
    void displayAvailability();
    
    /**
     * Check if parking space is available for a vehicle type.
     * @param type The vehicle type
     * @return true if space is available
     */
    boolean isSpaceAvailable(VehicleType type);
}
