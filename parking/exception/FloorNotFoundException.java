package com.parking.exception;

/**
 * Exception thrown when a specified floor is not found.
 */
public class FloorNotFoundException extends ParkingException {
    
    private final int floorNumber;
    
    public FloorNotFoundException(int floorNumber) {
        super("Floor " + floorNumber + " not found.");
        this.floorNumber = floorNumber;
    }
    
    public FloorNotFoundException(int floorNumber, String availableFloors) {
        super("Floor " + floorNumber + " not found. Available floors: " + availableFloors);
        this.floorNumber = floorNumber;
    }
    
    public int getFloorNumber() {
        return floorNumber;
    }
}
