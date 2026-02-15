package com.parking.exception;

/**
 * Exception thrown when an invalid ticket is used.
 */
public class InvalidTicketException extends ParkingException {
    
    public InvalidTicketException(String ticketId) {
        super("Invalid ticket: " + ticketId);
    }
}
