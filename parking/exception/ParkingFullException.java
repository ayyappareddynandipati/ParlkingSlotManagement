package com.parking.exception;

/**
 * Exception thrown when parking lot is full.
 */
public class ParkingFullException extends ParkingException {
    
    public ParkingFullException() {
        super("Parking lot is full. No slots available.");
    }
    
    public ParkingFullException(String vehicleType) {
        super("No slots available for " + vehicleType);
    }
}
