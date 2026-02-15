package com.parking.model;

/**
 * Two Wheeler vehicle (Motorcycle, Scooter)
 */
public class TwoWheeler extends Vehicle {
    
    private boolean hasHelmetStorage;
    
    public TwoWheeler(String vehicleNumber, String color) {
        super(vehicleNumber, color, VehicleType.TWO_WHEELER);
        this.hasHelmetStorage = false;
    }
    
    public TwoWheeler(String vehicleNumber, String color, boolean hasHelmetStorage) {
        super(vehicleNumber, color, VehicleType.TWO_WHEELER);
        this.hasHelmetStorage = hasHelmetStorage;
    }
    
    public boolean hasHelmetStorage() {
        return hasHelmetStorage;
    }
}
