package com.parking.exception;

/**
 * Exception thrown when a vehicle is not found in parking records.
 */
public class VehicleNotFoundException extends ParkingException {
    
    public VehicleNotFoundException(String vehicleNumber) {
        super("Vehicle " + vehicleNumber + " is not found in parking records.");
    }
    
    public VehicleNotFoundException(String vehicleNumber, String message) {
        super(message);
    }
}
