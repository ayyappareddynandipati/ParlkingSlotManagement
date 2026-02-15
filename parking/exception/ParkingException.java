package com.parking.exception;

/**
 * Base exception for parking lot operations.
 */
public class ParkingException extends RuntimeException {
    
    public ParkingException(String message) {
        super(message);
    }
    
    public ParkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
