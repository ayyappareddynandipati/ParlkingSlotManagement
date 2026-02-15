package com.parking.model;

/**
 * Factory for creating Vehicle objects.
 * Follows Factory Pattern for object creation.
 */
public class VehicleFactory {
    
    public static Vehicle createVehicle(String vehicleNumber, String color, VehicleType type) {
        switch (type) {
            case TWO_WHEELER:
                return new TwoWheeler(vehicleNumber, color);
            case FOUR_WHEELER:
                return new FourWheeler(vehicleNumber, color);
            case SIX_WHEELER:
                return new SixWheeler(vehicleNumber, color);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
    
    public static Vehicle createVehicle(String vehicleNumber, String color, int typeCode) {
        VehicleType type = VehicleType.fromCode(typeCode);
        if (type == null) {
            throw new IllegalArgumentException("Invalid vehicle type code: " + typeCode);
        }
        return createVehicle(vehicleNumber, color, type);
    }
}
