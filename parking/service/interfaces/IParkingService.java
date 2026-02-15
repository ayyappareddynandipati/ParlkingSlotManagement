package com.parking.service.interfaces;

import com.parking.model.Ticket;
import com.parking.model.Vehicle;
import com.parking.model.VehicleType;

/**
 * Interface for Parking Service operations.
 * Dependency Inversion: Gate handlers and controllers depend on this abstraction.
 */
public interface IParkingService {
    
    /**
     * Park a vehicle in the parking lot.
     * @param vehicle The vehicle to park
     * @return The parking ticket, or null if parking failed
     */
    Ticket parkVehicle(Vehicle vehicle);
    
    /**
     * Park a vehicle using vehicle number and type code (legacy support).
     * @param vehicleNumber The vehicle registration number
     * @param typeCode The vehicle type code (2, 4, or 6)
     */
    void parkVehicle(String vehicleNumber, int typeCode);
    
    /**
     * Unpark a vehicle using ticket ID.
     * @param ticketId The parking ticket ID
     * @return true if unparking was successful
     */
    boolean unparkVehicle(String ticketId);
    
    /**
     * Check if a vehicle is currently parked.
     * @param vehicleNumber The vehicle registration number
     * @return true if vehicle is parked
     */
    boolean isVehicleParked(String vehicleNumber);
    
    /**
     * Get ticket by ticket ID.
     * @param ticketId The ticket ID
     * @return The ticket, or null if not found
     */
    Ticket getTicket(String ticketId);
    
    /**
     * Find ticket by vehicle number.
     * @param vehicleNumber The vehicle registration number
     * @return The ticket, or null if not found
     */
    Ticket findTicketByVehicle(String vehicleNumber);
    
    /**
     * Get available slots for a vehicle type.
     * @param type The vehicle type
     * @return Number of available slots
     */
    int getAvailableSlots(VehicleType type);
    
    /**
     * Display current parking lot availability.
     */
    void displayAvailability();
    
    /**
     * Display floor-wise availability.
     */
    void displayFloorAvailability();
    
    /**
     * Set the slot allocation strategy.
     * @param strategy The strategy to use
     */
    void setParkingStrategy(SlotAllocationStrategy strategy);
    
    /**
     * Set the fee calculator.
     * @param calculator The fee calculator to use
     */
    void setFeeCalculator(FeeCalculator calculator);
}
