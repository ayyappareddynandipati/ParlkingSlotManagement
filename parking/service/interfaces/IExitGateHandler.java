package com.parking.service.interfaces;

/**
 * Interface for Exit Gate Handler operations.
 * Dependency Inversion: Controllers depend on this abstraction for exit operations.
 */
public interface IExitGateHandler extends IGateHandler {
    
    /**
     * Process vehicle exit through this gate.
     * @param ticketId The parking ticket ID
     * @return true if exit was successful
     */
    boolean processExit(String ticketId);
    
    /**
     * Process exit when ticket is lost.
     * @param vehicleNumber The vehicle registration number
     * @return true if exit was successful
     */
    boolean processLostTicket(String vehicleNumber);
}
