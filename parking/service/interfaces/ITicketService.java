package com.parking.service.interfaces;

import com.parking.model.Ticket;
import com.parking.model.VehicleType;
import java.util.List;

/**
 * Interface for Ticket Service operations.
 * Handles ticket generation, validation, and management.
 */
public interface ITicketService {
    
    /**
     * Generate a new parking ticket.
     * @param vehicleNumber The vehicle registration number
     * @param vehicleType The vehicle type
     * @param slotId The assigned slot ID
     * @param floorNumber The floor number
     * @return The generated ticket
     */
    Ticket generateTicket(String vehicleNumber, VehicleType vehicleType, String slotId, int floorNumber);
    
    /**
     * Validate a ticket.
     * @param ticketId The ticket ID to validate
     * @return true if ticket is valid and active
     */
    boolean validateTicket(String ticketId);
    
    /**
     * Close a ticket after vehicle exit.
     * @param ticketId The ticket ID
     * @param fare The calculated fare
     * @return true if ticket was closed successfully
     */
    boolean closeTicket(String ticketId, double fare);
    
    /**
     * Get ticket by ID.
     * @param ticketId The ticket ID
     * @return The ticket, or null if not found
     */
    Ticket getTicket(String ticketId);
    
    /**
     * Find ticket by vehicle number.
     * @param vehicleNumber The vehicle registration number
     * @return The ticket, or null if not found
     */
    Ticket findByVehicleNumber(String vehicleNumber);
    
    /**
     * Get all active tickets.
     * @return List of active tickets
     */
    List<Ticket> getActiveTickets();
    
    /**
     * Get all archived tickets.
     * @return List of archived tickets
     */
    List<Ticket> getArchivedTickets();
    
    /**
     * Mark ticket as lost.
     * @param ticketId The ticket ID
     * @return true if marked successfully
     */
    boolean markAsLost(String ticketId);
}
