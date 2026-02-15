package com.parking.model;

/**
 * Six Wheeler vehicle (Bus, Truck)
 */
public class SixWheeler extends Vehicle {
    
    private double loadCapacity; // in tons
    
    public SixWheeler(String vehicleNumber, String color) {
        super(vehicleNumber, color, VehicleType.SIX_WHEELER);
        this.loadCapacity = 0;
    }
    
    public SixWheeler(String vehicleNumber, String color, double loadCapacity) {
        super(vehicleNumber, color, VehicleType.SIX_WHEELER);
        this.loadCapacity = loadCapacity;
    }
    
    public double getLoadCapacity() {
        return loadCapacity;
    }
}
