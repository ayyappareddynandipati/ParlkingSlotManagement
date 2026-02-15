package com.parking.exception;

/**
 * Exception thrown when an operation requires parked vehicles but none exist.
 */
public class NoVehiclesParkedException extends ParkingException {
    
    public NoVehiclesParkedException() {
        super("No vehicles are currently parked.");
    }
    
    public NoVehiclesParkedException(String message) {
        super(message);
    }
}
