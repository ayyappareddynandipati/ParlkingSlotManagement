package com.parking.model;

/**
 * Four Wheeler vehicle (Car, SUV)
 */
public class FourWheeler extends Vehicle {
    
    public FourWheeler(String vehicleNumber, String color) {
        super(vehicleNumber, color, VehicleType.FOUR_WHEELER);
    }
}
