package com.parking.model;

/**
 * Abstract Vehicle class - base for all vehicle types.
 * Follows Open/Closed Principle - easy to extend for new vehicle types.
 */
public abstract class Vehicle {
    
    private String vehicleNumber;
    private String color;
    private VehicleType vehicleType;
    
    public Vehicle(String vehicleNumber, String color, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.color = color;
        this.vehicleType = vehicleType;
    }
    
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    
    public String getColor() {
        return color;
    }
    
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    @Override
    public String toString() {
        return vehicleType + " [" + vehicleNumber + "]";
    }
}
