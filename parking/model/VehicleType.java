
package com.parking.model;

/**
 * Enum representing different vehicle types supported by the parking lot.
 * Each vehicle type has a code and a default hourly rate.
 */
public enum VehicleType {
    TWO_WHEELER(2, 10.0, "Motorcycle/Scooter"),
    FOUR_WHEELER(4, 20.0, "Car/SUV"),
    SIX_WHEELER(6, 30.0, "Bus/Truck");

    private final int code;
    private final double defaultHourlyRate;
    private final String description;

    VehicleType(int code, double defaultHourlyRate, String description) {
        this.code = code;
        this.defaultHourlyRate = defaultHourlyRate;
        this.description = description;
    }

    public int getCode() {
        return code;
    }
    
    public double getDefaultHourlyRate() {
        return defaultHourlyRate;
    }
    
    public String getDescription() {
        return description;
    }

    public static VehicleType fromCode(int code) {
        for (VehicleType v : values()) {
            if (v.code == code) return v;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return code + "W (" + description + ")";
    }
}
