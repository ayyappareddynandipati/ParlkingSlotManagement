package com.parking.exception;

/**
 * Exception thrown when a vehicle is already parked.
 */
public class VehicleAlreadyParkedException extends ParkingException {
    
    public VehicleAlreadyParkedException(String vehicleNumber) {
        super("Vehicle " + vehicleNumber + " is already parked.");
    }
}
