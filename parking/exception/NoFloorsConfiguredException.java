package com.parking.exception;

/**
 * Exception thrown when an operation requires floors but none are configured.
 */
public class NoFloorsConfiguredException extends ParkingException {
    
    public NoFloorsConfiguredException() {
        super("No floors configured. Please add floors first from Admin Mode.");
    }
    
    public NoFloorsConfiguredException(String message) {
        super(message);
    }
}
